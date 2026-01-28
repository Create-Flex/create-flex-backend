package com.mcn.in4.domain.attendance.service;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.repository.AttendanceRepository;
import com.mcn.in4.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Override
    public List<AttendanceResponseDto> getAttendanceByMemberId(Long memberId) {
        return attendanceRepository.findAllByMember_MemberIdOrderByAttendanceDateDesc(memberId).stream()
                .map(attendance -> AttendanceResponseDto.builder()
                        .attendanceId(attendance.getAttendanceId())
                        .attendanceDate(attendance.getAttendanceDate())
                        .attendanceStart(attendance.getAttendanceStart())
                        .attendanceEnd(attendance.getAttendanceEnd())
                        .attendanceStatus(attendance.getAttendanceStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void checkIn(Long memberId) {
        if (attendanceRepository.findByMember_MemberIdAndAttendanceDate(memberId, LocalDate.now()).isPresent()) {
            throw new IllegalStateException("이미 오늘 출근 처리가 되었습니다.");
        }

        // Member 객체 생성 (ID만으로 참조 생성)
        Member member = Member.builder().memberId(memberId).build();

        Attendance attendance = Attendance.builder()
                .member(member)
                .attendanceDate(LocalDate.now())
                .attendanceStart(LocalDateTime.now())
                .attendanceStatus("근무중")
                .build();

        attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public void checkOut(Long memberId) {
        Attendance attendance = attendanceRepository.findByMember_MemberIdAndAttendanceDate(memberId, LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("오늘 출근 기록이 없습니다."));

        if (attendance.getAttendanceEnd() != null) {
            throw new IllegalStateException("이미 퇴근 처리가 되었습니다.");
        }

        LocalDateTime endTime = LocalDateTime.now();
        String status = calculateStatus(attendance.getAttendanceStart(), endTime);

        attendance.completeWork(endTime, status);
    }

    private String calculateStatus(LocalDateTime start, LocalDateTime end) {
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        LocalTime nineAm = LocalTime.of(9, 0);
        LocalTime sixPm = LocalTime.of(18, 0);

        // 1. 미달 (Under): 출근 > 09:00 OR 퇴근 < 18:00
        if (startTime.isAfter(nineAm) || endTime.isBefore(sixPm)) {
            return "지각/조퇴/미달";
        }

        // 2. 초과 (Over): (퇴근 - 출근 - 휴게시간 1시간) > 8시간
        // 휴게시간 1시간(60분) 차감 가정
        long netWorkMinutes = Duration.between(start, end).toMinutes() - 60;
        if (netWorkMinutes > 8 * 60) {
            return "연장 근무";
        }

        // 3. 정상 (Normal)
        return "정상";
    }
}