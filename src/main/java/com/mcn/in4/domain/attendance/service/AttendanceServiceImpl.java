package com.mcn.in4.domain.attendance.service;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.dto.AttendanceDashboardDto;
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
/**
 * 근태 관리 서비스 구현체
 * 근태 조회 로직 및 출퇴근 시 유효성 검사, 상태 계산 등을 수행합니다.
 */
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Override
    public List<AttendanceResponseDto> getAttendance(Long memberId, LocalDate startDate, LocalDate endDate,
            String status) {
        // 근태 상태 필터링 (String -> Enum 변환)
        AttendanceStatus attendanceStatus = null;
        if (status != null && !status.isEmpty()) {
            String finalStatus = status;
            attendanceStatus = Arrays.stream(AttendanceStatus.values())
                    .filter(s -> s.getDescription().equals(finalStatus) || s.name().equals(finalStatus))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + finalStatus));
        }

        // Repository 조회 후 DTO 변환
        return attendanceRepository.findAttendance(memberId, startDate, endDate, attendanceStatus).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceResponseDto> getAllAttendance(LocalDate startDate, LocalDate endDate, String status) {
        // 근태 상태 필터링 (String -> Enum 변환)
        AttendanceStatus attendanceStatus = null;
        if (status != null && !status.isEmpty()) {
            String finalStatus = status;
            attendanceStatus = Arrays.stream(AttendanceStatus.values())
                    .filter(s -> s.getDescription().equals(finalStatus) || s.name().equals(finalStatus))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + finalStatus));
        }

        // Repository 조회 후 DTO 변환
        return attendanceRepository.findAllAttendance(startDate, endDate, attendanceStatus).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void checkIn(Long memberId) {
        // 이미 오늘 출근 기록이 있는지 확인 (중복 출근 방지)
        if (attendanceRepository.findByMemberIdAndAttendanceDate(memberId, LocalDate.now()).isPresent()) {
            throw new IllegalStateException("이미 오늘 출근 처리가 되었습니다.");
        }

        // Member 객체 생성 (ID만으로 참조 생성)
        Member member = Member.builder().memberId(memberId).build();

        // 출근 기록 생성
        Attendance attendance = Attendance.builder()
                .member(member)
                .attendanceDate(LocalDate.now())
                .attendanceStart(LocalDateTime.now())
                .attendanceStatus(AttendanceStatus.WORKING) // 초기 상태는 '근무중'
                .build();

        attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public void checkOut(Long memberId) {
        // 오늘 출근 기록이 있는지 확인
        Attendance attendance = attendanceRepository.findByMemberIdAndAttendanceDate(memberId, LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("오늘 출근 기록이 없습니다."));

        // 이미 퇴근 처리가 되었는지 확인
        if (attendance.getAttendanceEnd() != null) {
            throw new IllegalStateException("이미 퇴근 처리가 되었습니다.");
        }

        LocalDateTime endTime = LocalDateTime.now();
        // 출퇴근 시간을 기반으로 근태 상태 계산 (지각, 조퇴 등)
        AttendanceStatus status = calculateStatus(attendance.getAttendanceStart(), endTime);

        // 퇴근 시간 및 상태 업데이트
        attendance.completeWork(endTime, status);
    }

    /**
     * 근태 상태 계산 메서드
     * 출근 시간과 퇴근 시간을 기준으로 정상, 지각, 조퇴 여부를 판단합니다.
     */
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

    /**
     * DTO 변환 메서드
     */
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

    @Override
    public AttendanceDashboardDto getMyDashboardStats(Long memberId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        // 이번 달의 모든 근태 기록 조회
        List<Attendance> attendances = attendanceRepository.findAttendance(memberId, startOfMonth, endOfMonth, null);

        int lateCount = 0;
        long totalOvertimeMinutes = 0;

        for (Attendance attendance : attendances) {
            // 지각 카운트
            if (attendance.getAttendanceStatus() == AttendanceStatus.LATE) {
                lateCount++;
            }

            // 초과 근무 계산 (근무 시간 - 9시간)
            if (attendance.getAttendanceStart() != null && attendance.getAttendanceEnd() != null) {
                Duration duration = Duration.between(attendance.getAttendanceStart(), attendance.getAttendanceEnd());
                long workedMinutes = duration.toMinutes();
                long standardMinutes = 9 * 60; // 9시간 = 540분

                if (workedMinutes > standardMinutes) {
                    // 초과한 분(minute)을 누적
                    totalOvertimeMinutes += (workedMinutes - standardMinutes);
                }
            }
        }

        return AttendanceDashboardDto.builder()
                .lateCount(lateCount)
                .totalOvertimeMinutes(totalOvertimeMinutes)
                .build();
    }
}