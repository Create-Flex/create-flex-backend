package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.VacationWorkation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacationWorkationRepository extends JpaRepository<VacationWorkation, Long> {

    Optional<VacationWorkation> findByVacationVacationId(Long vacationId);
}
