package com.mcn.in4.domain.chat.dto;

import com.mcn.in4.domain.chat.entity.ChatRoom;
import com.mcn.in4.domain.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ChatRoomResponseDto {
    private String roomId;
    private String name;
    private LocalDateTime createdAt;
    private List<MemberSimpleDto> members; // 참여자 목록

    public static ChatRoomResponseDto from(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .createdAt(chatRoom.getCreatedAt())
                .members(chatRoom.getMembers().stream()
                        .map(cm -> MemberSimpleDto.from(cm.getMember()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Builder
    public static class MemberSimpleDto {
        private Long memberId;
        private String memberName;


        public static MemberSimpleDto from(Member member) {
            return MemberSimpleDto.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .build();
        }
    }
}