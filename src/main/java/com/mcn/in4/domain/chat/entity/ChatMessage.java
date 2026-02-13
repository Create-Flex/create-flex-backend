package com.mcn.in4.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    // 어떤 방의 메시지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
    // 보낸 사람 (회원 ID 또는 이름)
    private String sender;
    // 메시지 내용
    @Column(columnDefinition = "TEXT")
    private String message;
    // 메시지 타입
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime sendDate;


    public enum MessageType {
        ENTER, TALK, EXIT
    }

}