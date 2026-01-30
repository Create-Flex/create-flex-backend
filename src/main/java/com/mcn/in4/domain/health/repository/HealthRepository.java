package com.mcn.in4.domain.health.repository;

import com.mcn.in4.domain.health.entity.Health;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {
}
