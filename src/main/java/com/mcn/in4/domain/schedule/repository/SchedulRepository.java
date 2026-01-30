package com.mcn.in4.domain.schedule.repository;

import com.mcn.in4.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulRepository extends JpaRepository<Schedule, Long> {
}
