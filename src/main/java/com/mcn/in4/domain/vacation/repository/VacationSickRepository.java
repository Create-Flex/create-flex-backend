package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.VacationSick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 병가 휴가 상세 Repository
 */
@Repository
public interface VacationSickRepository extends JpaRepository<VacationSick, Long> {

    /** 휴가 ID로 병가 상세 정보 조회 */
    Optional<VacationSick> findByVacationVacationId(Long vacationId);
}
