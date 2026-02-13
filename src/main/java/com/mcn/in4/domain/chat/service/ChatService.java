package com.mcn.in4.domain.chat.service;

import com.mcn.in4.domain.chat.dto.ChatMessageDto;
import com.mcn.in4.domain.chat.dto.ChatRoomResponseDto;
import com.mcn.in4.domain.chat.entity.ChatMessage;
import com.mcn.in4.domain.chat.entity.ChatRoom;

import java.util.List;

public interface ChatService {
    // 모든 채팅방 조회 (DTO 반환)
    List<ChatRoomResponseDto> findAllRoom();

    // 내 채팅방 조회
    List<ChatRoomResponseDto> findMyRooms(Long memberId);

    // 특정 채팅방 조회
    ChatRoom findRoomById(String roomId);

    // 채팅방 생성
    ChatRoomResponseDto createRoom(String name, List<Long> memberIds);

    // 메시지 저장
    ChatMessage saveMessage(ChatMessageDto messageDto);

    // 특정 방의 이전 대화 내역 조회
    List<ChatMessageDto> findMessages(String roomId);
}
