package com.mcn.in4.domain.chat.dto;

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
        ENTER, TALK, EXIT, MATCH, MATCH_REQUEST;
    }
    private MessageType type; // 메시지 타입
    private String roomId;    // 방 번호
    private String sender;    // 보낸 사람
    private String message;   // 메시지 내용

    private LocalDateTime sendDate;
}