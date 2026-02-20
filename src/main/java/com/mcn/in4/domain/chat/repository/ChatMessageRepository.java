package com.mcn.in4.domain.chat.repository;

import com.mcn.in4.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.chatRoom WHERE m.chatRoom.roomId = :roomId ORDER BY m.sendDate ASC")
    List<ChatMessage> findAllByRoomIdWithRoom(@Param("roomId") String roomId);

    List<ChatMessage> findByChatRoom_RoomIdOrderBySendDateAsc(String roomId);
}
