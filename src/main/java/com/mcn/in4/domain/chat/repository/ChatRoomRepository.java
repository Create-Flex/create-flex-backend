package com.mcn.in4.domain.chat.repository;

import com.mcn.in4.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    List<ChatRoom> findAllByOrderByCreatedAtDesc();
}
