package com.mcn.in4.domain.attendance.service;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.dto.AttendanceDashboardDto;
import com.mcn.in4.domain.attendance.dto.CompanyAttendanceDashboardDto;
import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.CheckInStatus;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.CheckOutStatus;
import com.mcn.in4.domain.attendance.repository.AttendanceRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
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
/**
 * 근태 관리 서비스 구현체
 * 근태 조회 로직 및 출퇴근 시 유효성 검사, 상태 계산 등을 수행합니다.
 */
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final VacationRepository vacationRepository;

    @Override
    public List<AttendanceResponseDto> getAttendance(Long memberId, LocalDate startDate, LocalDate endDate,
            String status) {
        // Repository 조회
        List<Attendance> attendances = attendanceRepository.findAttendanceByMemberId(memberId, startDate, endDate);

        // 상태 필터링 (status가 있으면 필터링)
        if (status != null && !status.isEmpty()) {
            attendances = attendances.stream()
                    .filter(a -> {
                        // "정상" 필터: 출근=NORMAL AND 퇴근=NORMAL
                        if ("NORMAL".equals(status)) {
                            return a.getCheckInStatus() == CheckInStatus.NORMAL
                                    && a.getCheckOutStatus() == CheckOutStatus.NORMAL;
                        }
                        // CheckInStatus 매칭
                        if (a.getCheckInStatus() != null && a.getCheckInStatus().name().equals(status)) {
                            return true;
                        }
                        // CheckOutStatus 매칭
                        if (a.getCheckOutStatus() != null && a.getCheckOutStatus().name().equals(status)) {
                            return true;
                        }
                        // 근무중 (퇴근 상태가 null인 경우)
                        if ("WORKING".equals(status) && a.getCheckOutStatus() == null && a.getCheckInStatus() != null) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        return attendances.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceResponseDto> getAllAttendance(LocalDate startDate, LocalDate endDate, String status,
            String name) {
        // Repository 조회 후 DTO 변환
        List<Attendance> attendances = attendanceRepository.findAllAttendance(startDate, endDate);

        // 이름 필터링 (name이 있으면 LIKE 검색)
        if (name != null && !name.isEmpty()) {
            String searchName = name.toLowerCase();
            attendances = attendances.stream()
                    .filter(a -> a.getMember().getMemberName().toLowerCase().contains(searchName))
                    .collect(Collectors.toList());
        }

        // 상태 필터링 (status가 있으면 checkInStatus 또는 checkOutStatus로 필터링)
        if (status != null && !status.isEmpty()) {
            attendances = attendances.stream()
                    .filter(a -> {
                        // "정상" 필터: 출근=NORMAL AND 퇴근=NORMAL
                        if ("NORMAL".equals(status)) {
                            return a.getCheckInStatus() == CheckInStatus.NORMAL
                                    && a.getCheckOutStatus() == CheckOutStatus.NORMAL;
                        }
                        // CheckInStatus 매칭
                        if (a.getCheckInStatus() != null && a.getCheckInStatus().name().equals(status)) {
                            return true;
                        }
                        // CheckOutStatus 매칭
                        if (a.getCheckOutStatus() != null && a.getCheckOutStatus().name().equals(status)) {
                            return true;
                        }
                        // 근무중 (퇴근 상태가 null인 경우)
                        if ("WORKING".equals(status) && a.getCheckOutStatus() == null && a.getCheckInStatus() != null) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        return attendances.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void checkIn(Long memberId) {
        // 이미 오늘 출근 기록이 있는지 확인 (중복 출근 방지)
        if (attendanceRepository.findByMemberIdAndAttendanceDate(memberId, LocalDate.now()).isPresent()) {
            throw new CustomException(ErrorCode.ATTENDANCE_ALREADY_MARKED);
        }

        // Member 객체 생성 (ID만으로 참조 생성)
        Member member = Member.builder().memberId(memberId).build();
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        // 오늘 승인된 휴가가 있는지 확인
        CheckInStatus checkInStatus;
        var vacationOpt = vacationRepository.findApprovedVacationByMemberAndDate(memberId, today);

        if (vacationOpt.isPresent()) {
            Vacation vacation = vacationOpt.get();
            VacationType type = vacation.getVacationType();

            switch (type) {
                case WORKATION:
                    checkInStatus = CheckInStatus.WORKATION;
                    break;
                case HALF:
                    // 오전 반차인 경우 (오후 출근) - 지각 면제
                    checkInStatus = CheckInStatus.HALF_VACATION;
                    break;
                case ANNUAL:
                case SICK:
                case FAMILY:
                    // 전일 휴가인데 출근 버튼을 눌렀다면
                    checkInStatus = CheckInStatus.VACATION;
                    break;
                default:
                    checkInStatus = now.toLocalTime().isAfter(LocalTime.of(9, 0))
                            ? CheckInStatus.LATE
                            : CheckInStatus.NORMAL;
            }
        } else {
            // 휴가 없음: 기존 로직 (9시 이후 = 지각)
            checkInStatus = now.toLocalTime().isAfter(LocalTime.of(9, 0))
                    ? CheckInStatus.LATE
                    : CheckInStatus.NORMAL;
        }

        // 출근 기록 생성
        Attendance attendance = Attendance.builder()
                .member(member)
                .attendanceDate(today)
                .attendanceStart(now)
                .checkInStatus(checkInStatus)
                .checkOutStatus(null) // 아직 퇴근 안 함
                .build();

        attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public void checkOut(Long memberId) {
        LocalDate today = LocalDate.now();

        // 오늘 출근 기록이 있는지 확인
        Attendance attendance = attendanceRepository.findByMemberIdAndAttendanceDate(memberId, today)
                .orElseThrow(() -> new CustomException(ErrorCode.ATTENDANCE_NOT_FOUND));

        // 이미 퇴근 처리가 되었는지 확인
        if (attendance.getAttendanceEnd() != null) {
            throw new CustomException(ErrorCode.ALREADY_CHECKED_OUT);
        }

        LocalDateTime endTime = LocalDateTime.now();
        CheckOutStatus checkOutStatus;

        // 오늘 승인된 휴가가 있는지 확인
        var vacationOpt = vacationRepository.findApprovedVacationByMemberAndDate(memberId, today);

        if (vacationOpt.isPresent()) {
            Vacation vacation = vacationOpt.get();
            VacationType type = vacation.getVacationType();

            switch (type) {
                case WORKATION:
                    // 워케이션은 시간 관계없이 워케이션 상태
                    checkOutStatus = CheckOutStatus.WORKATION;
                    break;
                case HALF:
                    // 오후 반차인 경우 (오후 퇴근) - 조퇴 면제
                    // 출근 상태가 HALF_VACATION이면 오전 반차이므로 일반 계산
                    if (attendance.getCheckInStatus() == CheckInStatus.HALF_VACATION) {
                        // 오전 반차: 일반 퇴근 로직 적용
                        checkOutStatus = calculateCheckOutStatus(endTime);
                    } else {
                        // 오후 반차: 조퇴 면제
                        checkOutStatus = CheckOutStatus.HALF_VACATION;
                    }
                    break;
                default:
                    checkOutStatus = calculateCheckOutStatus(endTime);
            }
        } else {
            // 휴가 없음: 기존 로직
            checkOutStatus = calculateCheckOutStatus(endTime);
        }

        // 퇴근 시간 및 상태 업데이트
        attendance.completeWork(endTime, checkOutStatus);
    }

    /**
     * 퇴근 상태 계산 메서드
     * 퇴근 시간을 기준으로 조퇴, 정상, 초과를 판단합니다.
     */
    private CheckOutStatus calculateCheckOutStatus(LocalDateTime end) {
        LocalTime endTime = end.toLocalTime();
        LocalTime sixPm = LocalTime.of(18, 0);

        // 조퇴: 퇴근 < 18:00
        if (endTime.isBefore(sixPm)) {
            return CheckOutStatus.EARLY_LEAVE;
        }

        // 초과: 퇴근 > 18:00
        if (endTime.isAfter(sixPm)) {
            return CheckOutStatus.OVERTIME;
        }

        // 정상: 퇴근 = 18:00
        return CheckOutStatus.NORMAL;
    }

    /**
     * DTO 변환 메서드
     */
    private AttendanceResponseDto toDto(Attendance attendance) {
        String workDuration = null;
        if (attendance.getAttendanceStart() != null && attendance.getAttendanceEnd() != null) {
            Duration duration = Duration.between(attendance.getAttendanceStart(), attendance.getAttendanceEnd());
            workDuration = formatMinutesToDuration(duration.toMinutes());
        }

        return AttendanceResponseDto.builder()
                .memberId(attendance.getMember().getMemberId())
                .memberName(attendance.getMember().getMemberName())
                .attendanceId(attendance.getAttendanceId())
                .attendanceDate(attendance.getAttendanceDate())
                .attendanceStart(attendance.getAttendanceStart())
                .attendanceEnd(attendance.getAttendanceEnd())
                .checkInStatus(attendance.getCheckInStatus() != null
                        ? attendance.getCheckInStatus().getDescription()
                        : null)
                .checkOutStatus(attendance.getCheckOutStatus() != null
                        ? attendance.getCheckOutStatus().getDescription()
                        : null)
                .workDuration(workDuration)
                .build();
    }

    @Override
    public AttendanceDashboardDto getMyDashboardStats(Long memberId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        // 이번 달의 모든 근태 기록 조회
        List<Attendance> attendances = attendanceRepository.findAttendanceByMemberId(memberId, startOfMonth,
                endOfMonth);

        int lateCount = 0;
        long totalOvertimeMinutes = 0;

        for (Attendance attendance : attendances) {
            // 지각 카운트 (checkInStatus로 집계)
            if (attendance.getCheckInStatus() == CheckInStatus.LATE) {
                lateCount++;
            }

            // 초과 근무 계산 (근무 시간 - 9시간)
            if (attendance.getAttendanceStart() != null && attendance.getAttendanceEnd() != null) {
                Duration duration = Duration.between(attendance.getAttendanceStart(), attendance.getAttendanceEnd());
                long workedMinutes = duration.toMinutes();
                long standardMinutes = 9 * 60; // 9시간 = 540분

                if (workedMinutes > standardMinutes) {
                    totalOvertimeMinutes += (workedMinutes - standardMinutes);
                }
            }
        }

        return AttendanceDashboardDto.builder()
                .lateCount(lateCount)
                .totalOvertimeMinutes(totalOvertimeMinutes)
                .build();
    }

    @Override
    public CompanyAttendanceDashboardDto getCompanyDashboardStats() {
        // 전체 직원 수 조회 (크리에이터 제외)
        long totalEmployees = memberRepository.countByMemberRoleNot(MemberRole.CREATOR);

        // 오늘 근태 현황 조회
        LocalDate today = LocalDate.now();
        List<Attendance> todayAttendances = attendanceRepository.findAllByAttendanceDate(today);

        // 크리에이터 제외하고 필터링
        List<Attendance> validTodayAttendances = todayAttendances.stream()
                .filter(a -> a.getMember().getMemberRole() != MemberRole.CREATOR)
                .collect(Collectors.toList());

        int todayNormalCount = 0;
        int todayLateCount = 0;

        for (Attendance att : validTodayAttendances) {
            CheckInStatus checkInStatus = att.getCheckInStatus();
            if (checkInStatus == CheckInStatus.NORMAL) {
                todayNormalCount++;
            } else if (checkInStatus == CheckInStatus.LATE) {
                todayLateCount++;
            }
        }

        // 결근 수 = 전체 직원 수 - 오늘 출근 기록이 있는 직원 수
        int todayAbsentCount = (int) totalEmployees - validTodayAttendances.size();
        if (todayAbsentCount < 0)
            todayAbsentCount = 0;

        // 이번 달 평균 시간 계산
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        List<Attendance> monthAttendances = attendanceRepository.findAllAttendance(startOfMonth, endOfMonth);

        // 크리에이터 제외
        List<Attendance> validMonthAttendances = monthAttendances.stream()
                .filter(a -> a.getMember().getMemberRole() != MemberRole.CREATOR)
                .collect(Collectors.toList());

        long totalStartMinutes = 0;
        int startCount = 0;
        long totalEndMinutes = 0;
        int endCount = 0;
        long totalWorkMinutes = 0;
        int workCount = 0;

        for (Attendance att : validMonthAttendances) {
            if (att.getAttendanceStart() != null) {
                totalStartMinutes += att.getAttendanceStart().toLocalTime().toSecondOfDay() / 60;
                startCount++;
            }
            if (att.getAttendanceEnd() != null) {
                totalEndMinutes += att.getAttendanceEnd().toLocalTime().toSecondOfDay() / 60;
                endCount++;

                Duration duration = Duration.between(att.getAttendanceStart(), att.getAttendanceEnd());
                totalWorkMinutes += duration.toMinutes();
                workCount++;
            }
        }

        String avgStartTime = (startCount > 0) ? formatMinutesToTime(totalStartMinutes / startCount) : "00:00";
        String avgEndTime = (endCount > 0) ? formatMinutesToTime(totalEndMinutes / endCount) : "00:00";
        String avgWorkTime = (workCount > 0) ? formatMinutesToDuration(totalWorkMinutes / workCount) : "0h 0m";

        return CompanyAttendanceDashboardDto.builder()
                .averageStartTime(avgStartTime)
                .averageEndTime(avgEndTime)
                .averageWorkTime(avgWorkTime)
                .todayNormalCount(todayNormalCount)
                .todayLateCount(todayLateCount)
                .todayAbsentCount(todayAbsentCount)
                .build();
    }

    private String formatMinutesToTime(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    private String formatMinutesToDuration(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%dh %02dm", hours, minutes);
    }
}