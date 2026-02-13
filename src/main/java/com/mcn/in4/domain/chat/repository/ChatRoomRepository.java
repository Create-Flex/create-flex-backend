package com.mcn.in4.domain.chat.repository;

import com.mcn.in4.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    List<ChatRoom> findAllByOrderByCreatedAtDesc();

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.members m WHERE m.member.memberId = :memberId ORDER BY cr.createdAt DESC")
    List<ChatRoom> findAllByMemberId(@Param("memberId") Long memberId);
}
