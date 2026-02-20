package com.mcn.in4.domain.chat.controller;

import com.mcn.in4.domain.chat.dto.ChatMessageDto;
import com.mcn.in4.domain.chat.dto.ChatRoomCreateRequest;
import com.mcn.in4.domain.chat.dto.ChatRoomDto;
import com.mcn.in4.domain.chat.dto.ChatRoomResponseDto;
import com.mcn.in4.domain.chat.entity.ChatMessage;
import com.mcn.in4.domain.chat.entity.ChatRoom;
import com.mcn.in4.domain.chat.service.ChatService;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message, Principal principal) {

         if (principal != null) {

             try{
                 message.setSenderId(Long.parseLong(principal.getName()));
             }catch (NumberFormatException e) {
                 log.error("principal 에서 이름을 찾지못함", e);
             }
         }

        // 입장 메시지 처리
        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        chatService.saveMessage(message);

        // 구독자들에게 전송 (/sub/chat/room/{roomId})
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

    }

    // 채팅방 생성
    @PostMapping("/chat/room")
    @ResponseBody
    public ChatRoomResponseDto createRoom(@RequestBody ChatRoomCreateRequest request) {

        return chatService.createRoom(request.getName(), request.getMemberIds());
    }

    // 채팅방 전체 조회
    @GetMapping("/chat/rooms")
    @ResponseBody
    public List<ChatRoomResponseDto> room() {
        return chatService.findAllRoom();
    }

    // 내 채팅방 조회
    @GetMapping("/chat/rooms/my")
    @ResponseBody
    public List<ChatRoomResponseDto> myRooms(@AuthenticationPrincipal String userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Long memberId = Long.parseLong(userId);
        return chatService.findMyRooms(memberId);
    }

    /// 특정 채팅방의 지난 대화 내용 조회
    @GetMapping("/chat/room/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> getMessages(@PathVariable String roomId, @AuthenticationPrincipal String memberIdStr) {

        Long memberId = Long.parseLong(memberIdStr);


        return chatService.findMessages(roomId, memberId);

    }
}
