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

    /** 관리자용 전체 휴가 목록 조회 (기간, 승인상태, 이름, 휴가유형 필터 적용) */
    @Query("SELECT v FROM Vacation v " +
           "WHERE v.vacationRequest BETWEEN :startDate AND :endDate " +
           "AND (:status IS NULL OR v.vacationApprove = :status) " +
           "AND (:name IS NULL OR :name = '' OR v.member.memberName LIKE %:name%) " +
           "AND (:type IS NULL OR v.vacationType = :type) " +
           "ORDER BY v.vacationRequest DESC")
    List<Vacation> findAllWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") VacationApprove status,
            @Param("name") String name,
            @Param("type") VacationType type
    );


    /** 오늘 날짜가 휴가 기간 내에 있고 승인된 상태인 휴가자 수 집계 */
    @Query("SELECT COUNT(v) FROM Vacation v " +
            "WHERE v.vacationApprove = :approveStatus " +
            "AND :today BETWEEN v.vacationStart AND v.vacationEnd")
    long countActiveVacations(@Param("today") LocalDate today,
                              @Param("approveStatus") VacationApprove approveStatus);

    /** 이번달 휴가자 수 (승인된 휴가 중 휴가 기간이 해당 월과 겹치는 건수) */
    @Query("SELECT COUNT(v) FROM Vacation v " +
           "WHERE v.vacationApprove = :status " +
           "AND v.vacationStart <= :monthEnd " +
           "AND v.vacationEnd >= :monthStart")
    long countMonthlyVacations(
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd,
            @Param("status") VacationApprove status
    );

    /** 총 미승인 대기자 수 */
    @Query("SELECT COUNT(v) FROM Vacation v WHERE v.vacationApprove = :status")
    long countByApproveStatus(@Param("status") VacationApprove status);

    /** 이번달 병가자 수 (승인된 병가 중 휴가 기간이 해당 월과 겹치는 건수) */
    @Query("SELECT COUNT(v) FROM Vacation v " +
           "WHERE v.vacationType = :type " +
           "AND v.vacationApprove = :status " +
           "AND v.vacationStart <= :monthEnd " +
           "AND v.vacationEnd >= :monthStart")
    long countMonthlyByType(
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd,
            @Param("type") VacationType type,
            @Param("status") VacationApprove status
    );

    /** 특정 회원이 특정 날짜에 승인된 휴가 중인지 확인 */
    @Query("SELECT COUNT(v) > 0 FROM Vacation v " +
            "WHERE v.member.memberId = :memberId " +
            "AND v.vacationApprove = :approveStatus " +
            "AND :today BETWEEN v.vacationStart AND v.vacationEnd")
    boolean isMemberOnVacation(@Param("memberId") Long memberId,
                               @Param("today") LocalDate today,
                               @Param("approveStatus") VacationApprove approveStatus);
}
