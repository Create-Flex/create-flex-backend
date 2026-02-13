package com.mcn.in4.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @Column(name = "room_id")
    private String roomId;
    @Column(name = "room_name", nullable = false)
    private String name;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    //  채팅방 참여자 목록 관리
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatRoomMember> members = new ArrayList<>();
    public static ChatRoom create(String name) {
        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .name(name)
                .build();
    }
    // 멤버 초대/추가 편의 메서드
    public void addMember(ChatRoomMember chatRoomMember) {
        this.members.add(chatRoomMember);
    }
}