package com.mcn.in4.domain.chat.controller;

import com.mcn.in4.domain.chat.dto.ChatMessageDto;
import com.mcn.in4.domain.chat.dto.ChatRoomDto;
import com.mcn.in4.domain.chat.entity.ChatMessage;
import com.mcn.in4.domain.chat.entity.ChatRoom;
import com.mcn.in4.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message, Principal principal) {
    // 로그인한 사용자 정보 가져오기 (JWT 인증 가정)
        if (principal != null) {
            message.setSender(principal.getName());
        }
        // 입장 메시지 처리
        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        chatService.saveMessage(message);

        //  구독자들에게 전송 (/sub/chat/room/{roomId})
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

    }
    //채팅방 생성
    @PostMapping("/chat/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    //채팅방 조회
    @GetMapping("/chat/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatService.findAllRoom();
    }

    ///특정 채팅방의 지난 대화 내용 조회
    @GetMapping("/chat/room/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> getMessages(@PathVariable String roomId) {
        //  Service에서 Entity 리스트 조회
        List<ChatMessage> entities = chatService.findMessages(roomId);
        // Entity -> DTO 변환
        return entities.stream()
                .map(entity -> ChatMessageDto.builder()
                        .roomId(roomId) // 파라미터로 받은 roomId 사용 (Entity 접근 최소화)
                        .sender(entity.getSender())
                        .message(entity.getMessage())
                        .type(ChatMessageDto.MessageType.valueOf(entity.getType().name()))
                        .sendDate(entity.getSendDate())
                        .build())
                .collect(Collectors.toList());
    }
}
