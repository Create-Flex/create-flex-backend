package com.mcn.in4.domain.attendance.controller.api;

import com.mcn.in4.domain.attendance.dto.AttendanceDashboardDto;
import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.dto.CompanyAttendanceDashboardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "Attendance", description = "근태 관리 API")
public interface AttendanceApi {

        @Operation(summary = "내 근태 기록 조회", description = "로그인한 사용자의 근태 기록을 조회합니다. 크리에이터는 조회할 수 없습니다.")
        @ApiResponse(responseCode = "200", description = "근태 기록 조회 성공")
        @ApiResponse(responseCode = "403", description = "권한 없음 (크리에이터)")
        ResponseEntity<Page<AttendanceResponseDto>> getAttendance(
                        @Parameter(hidden = true) @AuthenticationPrincipal String userId,
                        @Parameter(hidden = true) Authentication authentication,
                        @Parameter(description = "조회 시작 날짜 (선택)", example = "2024-01-01") @RequestParam(value = "startDate", required = false) String startDate,
                        @Parameter(description = "조회 종료 날짜 (선택)", example = "2024-01-31") @RequestParam(value = "endDate", required = false) String endDate,
                        @Parameter(description = "근태 상태 (선택)") @RequestParam(value = "status", required = false) String status,
                        @Parameter(hidden = true) Pageable pageable);

        @Operation(summary = "내 근태 통계 조회 (대시보드용)", description = "이번 달의 지각 횟수와 초과 근무 횟수를 반환합니다.")
        @ApiResponse(responseCode = "200", description = "대시보드 통계 조회 성공")
        @ApiResponse(responseCode = "403", description = "권한 없음 (크리에이터)")
        ResponseEntity<AttendanceDashboardDto> getMyDashboardStats(
                        @Parameter(hidden = true) @AuthenticationPrincipal String userId,
                        @Parameter(hidden = true) Authentication authentication);

        @Operation(summary = "전사 근태 통계 조회 (관리자용)", description = "전사 평균 출퇴근/근무 시간 및 오늘 근태 현황을 반환합니다. 관리자 권한이 필요합니다.")
        @ApiResponse(responseCode = "200", description = "전사 대시보드 통계 조회 성공")
        @ApiResponse(responseCode = "403", description = "권한 없음 (관리자 아님)")
        ResponseEntity<CompanyAttendanceDashboardDto> getCompanyDashboardStats(
                        @Parameter(hidden = true) Authentication authentication);

        @Operation(summary = "전체 직원 근태 기록 조회 (관리자용)", description = "모든 직원의 근태 기록을 조회합니다. 관리자 권한이 필요합니다.")
        @ApiResponse(responseCode = "200", description = "전체 근태 기록 조회 성공")
        @ApiResponse(responseCode = "403", description = "권한 없음 (관리자 아님)")
        ResponseEntity<Page<AttendanceResponseDto>> getAllAttendance(
                        @Parameter(hidden = true) @AuthenticationPrincipal String userId,
                        @Parameter(hidden = true) Authentication authentication,
                        @Parameter(description = "조회 시작 날짜 (선택)", example = "2024-01-01") @RequestParam(value = "startDate", required = false) String startDate,
                        @Parameter(description = "조회 종료 날짜 (선택)", example = "2024-01-31") @RequestParam(value = "endDate", required = false) String endDate,
                        @Parameter(description = "근태 상태 (선택)") @RequestParam(value = "status", required = false) String status,
                        @Parameter(description = "직원 이름 검색 (선택)") @RequestParam(value = "name", required = false) String name,
                        @Parameter(hidden = true) Pageable pageable);

        @Operation(summary = "출근 처리", description = "현재 시간으로 출근을 기록합니다. 크리에이터는 출근 처리를 할 수 없습니다.")
        @ApiResponse(responseCode = "200", description = "출근 처리 성공 - { attendanceStart: 'yyyy-MM-ddTHH:mm:ss' }")
        @ApiResponse(responseCode = "400", description = "이미 출근 처리됨")
        @ApiResponse(responseCode = "403", description = "권한 없음 (크리에이터)")
        ResponseEntity<?> checkIn(
                        @Parameter(hidden = true) @AuthenticationPrincipal String userId,
                        @Parameter(hidden = true) Authentication authentication);

        @Operation(summary = "퇴근 처리", description = "현재 시간으로 퇴근을 기록합니다. 크리에이터는 퇴근 처리를 할 수 없습니다.")
        @ApiResponse(responseCode = "200", description = "퇴근 처리 성공")
        @ApiResponse(responseCode = "400", description = "이미 퇴근 처리됨 또는 출근 기록 없음")
        @ApiResponse(responseCode = "403", description = "권한 없음 (크리에이터)")
        ResponseEntity<String> checkOut(
                        @Parameter(hidden = true) @AuthenticationPrincipal String userId,
                        @Parameter(hidden = true) Authentication authentication);
}
