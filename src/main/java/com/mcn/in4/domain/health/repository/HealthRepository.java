package com.mcn.in4.domain.health.repository;

import com.mcn.in4.domain.health.entity.Health;
import com.mcn.in4.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {
    List<Health> findByMemberIdAndCheckupDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);
}
