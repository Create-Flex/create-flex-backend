package com.mcn.in4.domain.attendance.repository;

import com.mcn.in4.domain.attendance.entity.Attendance;

import com.mcn.in4.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
/**
 * 근태 관리 리포지토리
 * 근태 데이터의 DB 조회 및 저장을 담당합니다.
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

        // 직원 엔티티와 날짜로 조회
        @Query("SELECT a FROM Attendance a WHERE a.member = :member AND a.attendanceDate = :attendanceDate")
        Optional<Attendance> findByMemberAndAttendanceDate(Member member, LocalDate attendanceDate);

        // 특정 날짜의 모든 근태 기록을 한번에 조회함, 직원 통계(오늘 근태를 가져올때 사용)
        @Query("SELECT a FROM Attendance a JOIN FETCH a.member WHERE a.attendanceDate = :attendanceDate")
        List<Attendance> findAllByAttendanceDate(@Param("attendanceDate") LocalDate attendanceDate);

        /**
         * 직원 ID와 날짜로 근태 기록 조회
         */
        @Query("SELECT a FROM Attendance a WHERE a.member.memberId = :memberId AND a.attendanceDate = :attendanceDate")
        Optional<Attendance> findByMemberIdAndAttendanceDate(
                        @Param("memberId") Long memberId,
                        @Param("attendanceDate") LocalDate attendanceDate);

        /**
         * 조건별 근태 조회 (특정 직원)
         */
        @Query("SELECT a FROM Attendance a JOIN FETCH a.member WHERE a.member.memberId = :memberId " +
                        "AND (:startDate IS NULL OR a.attendanceDate >= :startDate) " +
                        "AND (:endDate IS NULL OR a.attendanceDate <= :endDate) " +
                        "ORDER BY a.attendanceDate DESC")
        List<Attendance> findAttendanceByMemberId(
                        @Param("memberId") Long memberId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        /**
         * 조건별 전체 근태 조회 (관리자용)
         */
        @Query("SELECT a FROM Attendance a JOIN FETCH a.member WHERE " +
                        "(:startDate IS NULL OR a.attendanceDate >= :startDate) " +
                        "AND (:endDate IS NULL OR a.attendanceDate <= :endDate) " +
                        "ORDER BY a.attendanceDate DESC")
        List<Attendance> findAllAttendance(
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);


        /*
        특정 날짜 + 여러 회원의 근태 기록을 한 번에 조회 (IN 절 + Fetch Join)
        * */
    @Query("SELECT a FROM Attendance a JOIN FETCH a.member WHERE a.attendanceDate = :attendanceDate AND a.member.memberId IN :memberIds")
    List<Attendance> findByAttendanceDateAndMemberMemberIdIn(
            @Param("attendanceDate") LocalDate attendanceDate,
            @Param("memberIds") List<Long> memberIds);
}