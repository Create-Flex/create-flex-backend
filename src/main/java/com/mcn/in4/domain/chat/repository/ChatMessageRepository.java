package com.mcn.in4.domain.chat.repository;

import com.mcn.in4.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.chatRoom WHERE m.chatRoom.roomId = :roomId ORDER BY m.sendDate ASC")
    List<ChatMessage> findAllByRoomIdWithRoom(@Param("roomId") String roomId);

    List<ChatMessage> findByChatRoom_RoomIdOrderBySendDateAsc(String roomId);

    // 최신 메시지 조회 처음 들어갔을때
    @Query("SELECT m FROM ChatMessage m WHERE m.chatRoom.roomId = :roomId ORDER BY m.id DESC")
    List<ChatMessage> findLatestMessages(@Param("roomId") String roomId, Pageable pageable);
    // 특정 ID보다 작은 메시지 조회 스크롤 업 했을떄
    @Query("SELECT m FROM ChatMessage m WHERE m.chatRoom.roomId = :roomId AND m.id < :lastId ORDER BY m.id DESC")
    List<ChatMessage> findOlderMessages(@Param("roomId") String roomId, @Param("lastId") Long lastId, Pageable pageable);
}
