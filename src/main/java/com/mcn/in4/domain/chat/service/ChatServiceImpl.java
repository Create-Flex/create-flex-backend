package com.mcn.in4.domain.chat.service;

import com.mcn.in4.domain.chat.dto.ChatMessageDto;
import com.mcn.in4.domain.chat.dto.ChatRoomResponseDto;
import com.mcn.in4.domain.chat.entity.ChatMessage;
import com.mcn.in4.domain.chat.entity.ChatRoom;
import com.mcn.in4.domain.chat.entity.ChatRoomMember;
import com.mcn.in4.domain.chat.repository.ChatMessageRepository;
import com.mcn.in4.domain.chat.repository.ChatRoomMemberRepository;
import com.mcn.in4.domain.chat.repository.ChatRoomRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private Map<String, ChatRoom> chatRooms;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final SimpUserRegistry simpUserRegistry; // 채팅방을 구독하는 사람들의 id를 가져오기 위함
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> findAllRoom() {
        // 채팅방 생성순서 최근 순으로 반환
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByOrderByCreatedAtDesc();

        // Entity -> DTO 변환
        return chatRooms.stream()
                .map(ChatRoomResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> findMyRooms(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberId(memberId);
        return chatRooms.stream()
                .map(ChatRoomResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoom findRoomById(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    }

    @Transactional
    public ChatRoomResponseDto createRoom(String name, List<Long> memberIds) {
        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.create(name);

        // 멤버 조회 및 연결
        List<Member> members = memberRepository.findAllById(memberIds);

        for (Member member : members) {
            ChatRoomMember roomMember = ChatRoomMember.create(chatRoom, member);
            chatRoom.addMember(roomMember);
        }
        // 저장
        chatRoomRepository.save(chatRoom);
        return ChatRoomResponseDto.from(chatRoom);
    }

    @Override
    @Transactional
    public ChatMessage saveMessage(ChatMessageDto messageDto) {
        // 채팅방 조회
        ChatRoom chatRoom = findRoomById(messageDto.getRoomId());

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(messageDto.getSender()) // 이름
                .senderId(messageDto.getSenderId()) // id(pk값)
                .message(messageDto.getMessage())
                // DTO의 Enum을 Entity의 Enum으로 변환
                .type(ChatMessage.MessageType.valueOf(messageDto.getType().name()))
                .build();
        // 메세지 저장 및 발신자 읽음 처리
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        String roomId = messageDto.getRoomId();
        Long newMessageId = savedMessage.getId();
        Long senderId = messageDto.getSenderId();

        // 발신자 및 접속자 읽음 상태 업데이트
        List<Long> membersToUpdate = new ArrayList<>(findConnectedUsers(roomId));
        if (!membersToUpdate.contains(senderId)) {
            membersToUpdate.add(senderId);
        }
        chatRoomMemberRepository.updateLastReadMessageId(roomId, membersToUpdate, newMessageId);

        // 안읽은 인원수 계산
        long totalMembers = chatRoomMemberRepository.countByRoomId(roomId);
        long readMembers = chatRoomMemberRepository.countReadMembers(roomId, newMessageId);

        long unreadCount = Math.max(0, totalMembers - readMembers);
        messageDto.setUnreadCount(unreadCount);

        return savedMessage;

    }

    // //특정 채팅방에 현재 접속해 있는(구독 중인) 사용자들의 ID 목록을 반환
    // private List<Long> findConnectedUsers(String roomId) {
    // String destination = "/sub/chat/room/" + roomId;
    //
    // return simpUserRegistry.getUsers().stream()
    // .filter(user -> user.getSessions().stream()
    // .flatMap(session -> session.getSubscriptions().stream())
    // .anyMatch(sub -> destination.equals(sub.getDestination())))
    // .map(SimpUser::getName)
    // .map(this::parseLongSafe)
    // .filter(Objects::nonNull)
    // .collect(Collectors.toList());
    // }
    private List<Long> findConnectedUsers(String roomId) {
        String destination = "/sub/chat/room/" + roomId;

        // 디버깅 로그
        log.info(" findConnectedUsers 호출: roomId={}", roomId);
        log.info(" 찾는구독경로(Server): {}", destination);

        // SimpUserRegistry 전체 조회
        Set<SimpUser> users = simpUserRegistry.getUsers();
        log.info(" 현재 레지스트리에 등록된 총 유저 수: {}", users.size());
        for (SimpUser user : users) {
            log.info("   유저명(Principal.name): {}", user.getName());
            if (user.getSessions() != null) {
                for (SimpSession session : user.getSessions()) {
                    log.info("    SessionID: {}", session.getId());
                    if (session.getSubscriptions() != null) {
                        for (SimpSubscription sub : session.getSubscriptions()) {
                            log.info("   구독중인 경로(Client): {}", sub.getDestination());
                        }
                    }
                }
            }
        }

        return simpUserRegistry.getUsers().stream()
                .filter(user -> user.getSessions().stream()
                        .flatMap(session -> session.getSubscriptions().stream())
                        .anyMatch(sub -> destination.equals(sub.getDestination())))
                .map(SimpUser::getName)
                .map(this::parseLongSafe)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // String 값을 Long으로 변환. 변환 실패 시 null을 반환
    private Long parseLongSafe(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<ChatMessageDto> findMessages(String roomId, Long memberId, int size) {
        // 채팅방 진입 시 메시지 조회
        Pageable pageable = PageRequest.of(0, size);
        List<ChatMessage> messages = chatMessageRepository.findLatestMessages(roomId, pageable);

        if (messages.isEmpty()) {
            return new ArrayList<>();
        }

        // 시간순 정렬
        Collections.reverse(messages);

        // 페이지읽음 처리
        ChatMessage lastMessage = messages.get(messages.size() - 1);
        updateMyReadStatus(roomId, memberId, lastMessage.getId());

        // 메시지 DTO 변환 및 안읽은 인원 수 계산
        return convertToDtoWithUnreadCount(roomId, messages);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageDto> findOlderMessages(String roomId, Long lastId, int size) {
        // 이전 메세지 조회
        Pageable pageable = PageRequest.of(0, size);
        List<ChatMessage> messages = chatMessageRepository.findOlderMessages(roomId, lastId, pageable);

        if (messages.isEmpty()) {
            return new ArrayList<>();
        }

        // 시간순 정렬
        Collections.reverse(messages);

        // 메시지 DTO 변환 및 안읽은 인원 수 계산
        return convertToDtoWithUnreadCount(roomId, messages);
    }

    // 페이지읽음 처리
    private void updateMyReadStatus(String roomId, Long memberId, Long lastMessageId) {
        chatRoomMemberRepository.findByChatRoom_RoomIdAndMember_MemberId(roomId, memberId)
                .ifPresent(m -> {
                    Long lastReadId = m.getLastReadMessageId();
                    if (lastReadId == null || lastReadId < lastMessageId) {
                        chatRoomMemberRepository.updateMemberLastReadMessage(roomId, memberId, lastMessageId);

                        // 읽음 알림 이벤트 발행
                        ChatMessageDto readEvent = ChatMessageDto.builder()
                                .type(ChatMessageDto.MessageType.READ)
                                .roomId(roomId)
                                .senderId(memberId)
                                .message("READ_UPDATE")
                                .build();
                        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, readEvent);
                    }
                });
    }

    // 메시지 목록 변환 및 각 메시지별 안읽은 인원수 합산
    private List<ChatMessageDto> convertToDtoWithUnreadCount(String roomId, List<ChatMessage> messages) {
        List<ChatRoomMember> allMembers = chatRoomMemberRepository.findAllByRoomIdWithMemberAndRoom(roomId);
        long totalMembers = allMembers.size();

        return messages.stream()
                .map(msg -> {
                    ChatMessageDto dto = ChatMessageDto.from(msg);
                    long readCount = allMembers.stream()
                            .filter(m -> {
                                // 발신자 본인은 항상 읽음 처리하거나 마지막 읽은 메시지 확인
                                boolean isSender = m.getMember().getMemberId().equals(msg.getSenderId());
                                boolean hasRead = m.getLastReadMessageId() != null
                                        && m.getLastReadMessageId() >= msg.getId();
                                return isSender || hasRead;
                            })
                            .count();
                    long unreadCount = totalMembers - readCount;
                    dto.setUnreadCount(Math.max(0, unreadCount));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateRoomName(String roomId, String name) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        chatRoom.updateName(name);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    @Transactional
    public void leaveRoom(String roomId, Long memberId) {
        ChatRoomMember roomMember = chatRoomMemberRepository.findByChatRoom_RoomIdAndMember_MemberId(roomId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        chatRoomMemberRepository.delete(roomMember);
    }

}
