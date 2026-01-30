package com.mcn.in4.domain.attendance.controller;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.dto.AttendanceDashboardDto;
import com.mcn.in4.domain.attendance.dto.CompanyAttendanceDashboardDto;
import com.mcn.in4.domain.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
/**
 * 근태 관리 컨트롤러
 * 직원의 출퇴근 기록 및 근태 현황 조회를 담당하는 컨트롤러입니다.
 */
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * 내 근태 기록 조회
     * 로그인한 사용자의 근태 기록을 조회합니다.
     * 크리에이터는 조회할 수 없습니다.
     * 
     * @param userId         로그인한 사용자 ID
     * @param authentication 인증 정보
     * @param startDate      조회 시작 날짜 (선택)
     * @param endDate        조회 종료 날짜 (선택)
     * @param status         근태 상태 (선택)
     * @return 근태 기록 리스트
     */
    @GetMapping
    public ResponseEntity<List<AttendanceResponseDto>> getAttendance(
            @AuthenticationPrincipal String userId,
            Authentication authentication,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "status", required = false) String status) {

        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isCreator) {
            return ResponseEntity.status(403).build();
        }

        Long memberId = Long.parseLong(userId);

        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate)
                : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        return ResponseEntity.ok(attendanceService.getAttendance(memberId, start, end, status));
    }

    /**
     * 내 근태 통계 조회 (대시보드용)
     * 이번 달의 지각 횟수와 초과 근무 횟수를 반환합니다.
     * 
     * @param userId 로그인한 사용자 ID
     * @return 대시보드 통계 DTO
     */
    @GetMapping("/dashboard/my")
    public ResponseEntity<AttendanceDashboardDto> getMyDashboardStats(@AuthenticationPrincipal String userId,
            Authentication authentication) {
        log.info("Dashboard Request - UserID: {}, Authorities: {}", userId, authentication.getAuthorities());

        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isCreator) {
            log.warn("Access Denied: User is CREATOR");
            return ResponseEntity.status(403).body(null);
        }

        Long memberId = Long.parseLong(userId);
        return ResponseEntity.ok(attendanceService.getMyDashboardStats(memberId));
    }

    /**
     * 전사 근태 통계 조회 (관리자용)
     * 전사 평균 출퇴근/근무 시간 및 오늘 근태 현황을 반환합니다.
     * 관리자 권한이 필요합니다.
     *
     * @param authentication 인증 정보
     * @return 전사 대시보드 통계 DTO
     */
    @GetMapping("/dashboard/company")
    public ResponseEntity<CompanyAttendanceDashboardDto> getCompanyDashboardStats(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(
                        a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(attendanceService.getCompanyDashboardStats());
    }

    /**
     * 전체 직원 근태 기록 조회 (관리자용)
     * 모든 직원의 근태 기록을 조회합니다.
     * 관리자 권한이 필요합니다.
     * 
     * @param userId         로그인한 사용자 ID
     * @param authentication 인증 정보
     * @param startDate      조회 시작 날짜 (선택)
     * @param endDate        조회 종료 날짜 (선택)
     * @param status         근태 상태 (선택)
     * @return 전체 근태 기록 리스트
     */
    @GetMapping("/all")
    public ResponseEntity<List<AttendanceResponseDto>> getAllAttendance(
            @AuthenticationPrincipal String userId,
            Authentication authentication,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "status", required = false) String status) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate)
                : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        return ResponseEntity.ok(attendanceService.getAllAttendance(start, end, status));
    }

    /**
     * 출근 처리
     * 현재 시간으로 출근을 기록합니다.
     * 크리에이터는 출근 처리를 할 수 없습니다.
     * 
     * @param userId         로그인한 사용자 ID
     * @param authentication 인증 정보
     * @return 처리 결과 메시지
     */
    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(@AuthenticationPrincipal String userId,
            Authentication authentication) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isCreator) {
            return ResponseEntity.status(403).body("크리에이터는 출근 처리를 할 수 없습니다.");
        }

        attendanceService.checkIn(Long.parseLong(userId));
        return ResponseEntity.ok("출근 처리되었습니다.");
    }

    /**
     * 퇴근 처리
     * 현재 시간으로 퇴근을 기록하고 근태 상태를 계산합니다.
     * 크리에이터는 퇴근 처리를 할 수 없습니다.
     * 
     * @param userId         로그인한 사용자 ID
     * @param authentication 인증 정보
     * @return 처리 결과 메시지
     */
    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(@AuthenticationPrincipal String userId,
            Authentication authentication) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isCreator) {
            return ResponseEntity.status(403).body("크리에이터는 퇴근 처리를 할 수 없습니다.");
        }

        attendanceService.checkOut(Long.parseLong(userId));
        return ResponseEntity.ok("퇴근 처리되었습니다.");
    }
}