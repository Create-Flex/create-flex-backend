package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.VacationFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationFamilyRepository extends JpaRepository<VacationFamily, Long> {
}
