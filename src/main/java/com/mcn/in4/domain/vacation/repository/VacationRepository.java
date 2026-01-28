package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 휴가 신청 Repository
 */
@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

    /** 특정 회원의 전체 휴가 목록 조회 (신청일 기준 내림차순) */
    List<Vacation> findByMemberMemberIdOrderByVacationRequestDesc(Long memberId);

    /** 내 휴가 목록 조회 (기간, 유형 필터 적용) */
    @Query("SELECT v FROM Vacation v " +
           "WHERE v.member.memberId = :memberId " +
           "AND v.vacationRequest BETWEEN :startDate AND :endDate " +
           "AND (:type IS NULL OR v.vacationType = :type) " +
           "ORDER BY v.vacationRequest DESC")
    List<Vacation> findMyVacationsWithFilters(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("type") VacationType type
    );

    /** 관리자용 전체 휴가 목록 조회 (기간, 승인상태, 이름 필터 적용) */
    @Query("SELECT v FROM Vacation v " +
           "WHERE v.vacationRequest BETWEEN :startDate AND :endDate " +
           "AND (:status IS NULL OR v.vacationApprove = :status) " +
           "AND (:name IS NULL OR :name = '' OR v.member.memberName LIKE %:name%) " +
           "ORDER BY v.vacationRequest DESC")
    List<Vacation> findAllWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") VacationApprove status,
            @Param("name") String name
    );
}
