package com.mcn.in4.domain.health.repository;

import com.mcn.in4.domain.health.entity.Health;
import com.mcn.in4.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {
    List<Health> findByMember_MemberIdAndCheckupDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

    Optional<Health> findTopByMember_MemberIdAndCheckupDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);
}
