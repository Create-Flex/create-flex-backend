package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.VacationWorkation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 워케이션 휴가 상세 Repository
 */
@Repository
public interface VacationWorkationRepository extends JpaRepository<VacationWorkation, Long> {

    /** 휴가 ID로 워케이션 상세 정보 조회 */
    Optional<VacationWorkation> findByVacationVacationId(Long vacationId);
}
