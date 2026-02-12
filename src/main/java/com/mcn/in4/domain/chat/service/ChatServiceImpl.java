package com.mcn.in4.domain.chat.service;

import com.mcn.in4.domain.chat.dto.ChatMessageDto;
import com.mcn.in4.domain.chat.entity.ChatMessage;
import com.mcn.in4.domain.chat.entity.ChatRoom;
import com.mcn.in4.domain.chat.repository.ChatMessageRepository;
import com.mcn.in4.domain.chat.repository.ChatRoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private Map<String, ChatRoom> chatRooms;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }
    @Override
    public List<ChatRoom> findAllRoom() {
        // 채팅방 생성순서 최근 순으로 반환
        return chatRoomRepository.findAllByOrderByCreatedAtDesc();

    }
    @Override
    public ChatRoom findRoomById(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    }
    @Override
    @Transactional
    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    @Transactional
    public ChatMessage saveMessage(ChatMessageDto messageDto) {
         //채팅방 조회
        ChatRoom chatRoom = findRoomById(messageDto.getRoomId());


        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(messageDto.getSender())
                .message(messageDto.getMessage())
                // DTO의 Enum을 Entity의 Enum으로 변환 (이름이 같다고 가정)
                .type(ChatMessage.MessageType.valueOf(messageDto.getType().name()))
                .build();
        //  저장
        return chatMessageRepository.save(chatMessage);
    }
    @Override
    public List<ChatMessage> findMessages(String roomId) {
        // 메시지 내역 조회 (작성 시간 오름차순)
        return chatMessageRepository.findByChatRoom_RoomIdOrderBySendDateAsc(roomId);
    }
}