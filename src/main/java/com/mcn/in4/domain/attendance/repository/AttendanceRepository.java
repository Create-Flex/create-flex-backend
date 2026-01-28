package com.mcn.in4.domain.attendance.repository;

import com.mcn.in4.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByMember_MemberIdOrderByAttendanceDateDesc(Long memberId);
    Optional<Attendance> findByMember_MemberIdAndAttendanceDate(Long memberId, LocalDate attendanceDate);
}