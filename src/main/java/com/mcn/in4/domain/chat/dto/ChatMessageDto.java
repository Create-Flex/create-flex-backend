package com.mcn.in4.domain.chat.dto;

import com.mcn.in4.domain.chat.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    // 메시지 타입: 입장, 채팅, 퇴장 등
    public enum MessageType {
        ENTER, TALK, EXIT, MATCH, MATCH_REQUEST, READ;
    }

    private Long id; // 메시지 고유 ID
    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 보낸 사람
    private Long senderId; // 보낸사람 아이디
    private String message; // 메시지 내용
    private Long unreadCount; // 읽지않은 수

    private LocalDateTime sendDate;

    public static ChatMessageDto from(ChatMessage entity) {
        return ChatMessageDto.builder()
                .id(entity.getId())
                .type(MessageType.valueOf(entity.getType().name()))
                .roomId(entity.getChatRoom().getRoomId())
                .sender(entity.getSender())
                .senderId(entity.getSenderId())
                .message(entity.getMessage())
                .sendDate(entity.getSendDate())
                .build();
    }
}