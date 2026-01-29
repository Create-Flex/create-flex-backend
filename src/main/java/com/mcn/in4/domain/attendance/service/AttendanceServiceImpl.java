package com.mcn.in4.domain.attendance.service;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.AttendanceStatus;
import com.mcn.in4.domain.attendance.repository.AttendanceRepository;
import com.mcn.in4.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Override
    public List<AttendanceResponseDto> getAttendance(Long memberId, LocalDate startDate, LocalDate endDate,
            String status) {
        AttendanceStatus attendanceStatus = null;
        if (status != null && !status.isEmpty()) {
            String finalStatus = status;
            attendanceStatus = Arrays.stream(AttendanceStatus.values())
                    .filter(s -> s.getDescription().equals(finalStatus) || s.name().equals(finalStatus))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + finalStatus));
        }

        return attendanceRepository.findAttendance(memberId, startDate, endDate, attendanceStatus).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceResponseDto> getAllAttendance(LocalDate startDate, LocalDate endDate, String status) {
        AttendanceStatus attendanceStatus = null;
        if (status != null && !status.isEmpty()) {
            String finalStatus = status;
            attendanceStatus = Arrays.stream(AttendanceStatus.values())
                    .filter(s -> s.getDescription().equals(finalStatus) || s.name().equals(finalStatus))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + finalStatus));
        }

        return attendanceRepository.findAllAttendance(startDate, endDate, attendanceStatus).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void checkIn(Long memberId) {
        if (attendanceRepository.findByMemberIdAndAttendanceDate(memberId, LocalDate.now()).isPresent()) {
            throw new IllegalStateException("이미 오늘 출근 처리가 되었습니다.");
        }

        // Member 객체 생성 (ID만으로 참조 생성)
        Member member = Member.builder().memberId(memberId).build();

        Attendance attendance = Attendance.builder()
                .member(member)
                .attendanceDate(LocalDate.now())
                .attendanceStart(LocalDateTime.now())
                .attendanceStatus(AttendanceStatus.WORKING)
                .build();

        attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public void checkOut(Long memberId) {
        Attendance attendance = attendanceRepository.findByMemberIdAndAttendanceDate(memberId, LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("오늘 출근 기록이 없습니다."));

        if (attendance.getAttendanceEnd() != null) {
            throw new IllegalStateException("이미 퇴근 처리가 되었습니다.");
        }

        LocalDateTime endTime = LocalDateTime.now();
        AttendanceStatus status = calculateStatus(attendance.getAttendanceStart(), endTime);

        attendance.completeWork(endTime, status);
    }

    private AttendanceStatus calculateStatus(LocalDateTime start, LocalDateTime end) {
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();
        LocalTime nineAm = LocalTime.of(9, 0);
        LocalTime sixPm = LocalTime.of(18, 0);

        // 1. 지각 (Late): 출근 > 09:00
        if (startTime.isAfter(nineAm)) {
            return AttendanceStatus.LATE;
        }

        // 2. 조퇴 (Early Leave): 퇴근 < 18:00
        if (endTime.isBefore(sixPm)) {
            return AttendanceStatus.EARLY_LEAVE;
        }

        // 3. 정상 (Normal) -> '출근'
        return AttendanceStatus.NORMAL;
    }

    private AttendanceResponseDto toDto(Attendance attendance) {
        return AttendanceResponseDto.builder()
                .memberId(attendance.getMember().getMemberId())
                .memberName(attendance.getMember().getMemberName())
                .attendanceId(attendance.getAttendanceId())
                .attendanceDate(attendance.getAttendanceDate())
                .attendanceStart(attendance.getAttendanceStart())
                .attendanceEnd(attendance.getAttendanceEnd())
                .attendanceStatus(attendance.getAttendanceStatus().getDescription())
                .build();
    }
}