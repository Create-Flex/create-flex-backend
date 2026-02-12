package com.mcn.in4.domain.chat.repository;

import com.mcn.in4.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom_RoomIdOrderBySendDateAsc(String roomId);

}
