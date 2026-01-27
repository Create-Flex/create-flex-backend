package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.VacationSick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacationSickRepository extends JpaRepository<VacationSick, Long> {

    Optional<VacationSick> findByVacationVacationId(Long vacationId);
}
