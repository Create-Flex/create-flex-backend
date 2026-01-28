package com.mcn.in4.domain.attendance.repository;

import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 직원 엔티티와 날짜로 조회
    @Query("SELECT a FROM Attendance a WHERE a.member = :member AND a.attendanceDate = :attendanceDate")
    Optional<Attendance> findByMemberAndAttendanceDate(Member member, LocalDate attendanceDate);


    // 특정 날짜 의 모든 근태 기록을 한번에 조회함, 직원 통계(오늘 근태를 가져올때 사용)
    List<Attendance> findAllByAttendanceDate(LocalDate attendanceDate);



        @org.springframework.data.jpa.repository.Query("SELECT a FROM Attendance a WHERE a.member.memberId = :memberId AND a.attendanceDate = :attendanceDate")
        Optional<Attendance> findByMemberIdAndAttendanceDate(
                        @org.springframework.data.repository.query.Param("memberId") Long memberId,
                        @org.springframework.data.repository.query.Param("attendanceDate") LocalDate attendanceDate);

        @org.springframework.data.jpa.repository.Query("SELECT a FROM Attendance a WHERE a.member.memberId = :memberId "
                        +
                        "AND (:startDate IS NULL OR a.attendanceDate >= :startDate) " +
                        "AND (:endDate IS NULL OR a.attendanceDate <= :endDate) " +
                        "AND (:attendanceStatus IS NULL OR a.attendanceStatus = :attendanceStatus) " +
                        "ORDER BY a.attendanceDate DESC")
        List<Attendance> findAttendance(
                        @org.springframework.data.repository.query.Param("memberId") Long memberId,
                        @org.springframework.data.repository.query.Param("startDate") LocalDate startDate,
                        @org.springframework.data.repository.query.Param("endDate") LocalDate endDate,
                        @org.springframework.data.repository.query.Param("attendanceStatus") com.mcn.in4.domain.attendance.entity.attendanceEnum.AttendanceStatus attendanceStatus);

}