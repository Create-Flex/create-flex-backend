package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.VacationFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 경조사 휴가 상세 Repository
 */
@Repository
public interface VacationFamilyRepository extends JpaRepository<VacationFamily, Long> {

    /** 휴가 ID로 경조사 상세 정보 조회 */
    Optional<VacationFamily> findByVacationVacationId(Long vacationId);
}
