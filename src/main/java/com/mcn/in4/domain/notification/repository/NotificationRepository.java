package com.mcn.in4.domain.notification.repository;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMemberOrderByCreatedAtDesc(Member member);
}
