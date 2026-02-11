package com.mcn.in4.domain.chat.controller;

import com.mcn.in4.domain.chat.dto.ChatMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, Principal principal) {

        // StompHandler가 넣어준 memberName을 바로 꺼냄
        String senderName = principal.getName();
        // 메시지 보낸 사람 설정
        message.setSender(senderName);
        // 입장 메시지 처리
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(senderName + "님이 입장하셨습니다.");
        }

        //  구독자들에게 전송 (/sub/chat/room/{roomId})
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
