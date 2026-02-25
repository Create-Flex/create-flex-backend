package com.mcn.in4.domain.chat.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.*;
import java.time.LocalDateTime;
import com.mcn.in4.domain.member.entity.Member;

@Entity
@Table(name = "chat_room_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoomMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_member_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @CreationTimestamp
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "last_read_message_id")
    private Long lastReadMessageId;

    // 생성 메서드
    public static ChatRoomMember create(ChatRoom chatRoom, Member member) {
        return ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .lastReadMessageId(0L)
                .build();
    }
}