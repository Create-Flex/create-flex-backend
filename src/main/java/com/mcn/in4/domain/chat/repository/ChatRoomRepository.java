package com.mcn.in4.domain.chat.repository;

import com.mcn.in4.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

        @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
                        "LEFT JOIN FETCH cr.members crm " +
                        "LEFT JOIN FETCH crm.member m " +
                        "LEFT JOIN FETCH m.department " +
                        "ORDER BY cr.createdAt DESC")
        List<ChatRoom> findAllByOrderByCreatedAtDesc();

        @Query("SELECT DISTINCT cr FROM ChatRoom cr " +
                        "JOIN cr.members m " +
                        "LEFT JOIN FETCH cr.members crm " +
                        "LEFT JOIN FETCH crm.member mem " +
                        "LEFT JOIN FETCH mem.department " +
                        "WHERE m.member.memberId = :memberId " +
                        "ORDER BY cr.createdAt DESC")
        List<ChatRoom> findAllByMemberId(@Param("memberId") Long memberId);
}
