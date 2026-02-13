package com.mcn.in4.domain.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatRoomCreateRequest {
    private String name;
    private List<Long> memberIds; // 참여할 멤버들의 ID 리스트
}