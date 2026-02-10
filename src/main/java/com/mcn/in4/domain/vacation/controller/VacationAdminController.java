package com.mcn.in4.domain.vacation.controller;

import com.mcn.in4.domain.vacation.controller.api.VacationAdminApi;
import com.mcn.in4.domain.vacation.dto.request.VacationRejectRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationDetailResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationStatisticsResponseDTO;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.vacation.service.VacationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 휴가 관리 API 컨트롤러 (관리자용)
 * - GET /api/admin/vacations : 전체 휴가 목록 조회 (필터: 기간, 상태, 이름, 휴가유형)
 * - GET /api/admin/vacations/{id} : 휴가 상세 조회 (유형별 상세 정보 포함)
 * - GET /api/admin/vacations/statistics : 휴가 통계 조회
 * - PATCH /api/admin/vacations/{id}/approve : 휴가 승인
 * - PATCH /api/admin/vacations/{id}/reject : 휴가 반려
 */
@RestController
@RequestMapping("/api/admin/vacations")
@RequiredArgsConstructor
public class VacationAdminController implements VacationAdminApi {

    private final VacationAdminService vacationAdminService;

    /** 전체 휴가 목록 조회 (기간, 승인상태, 이름, 휴가유형 필터) - 관리자 전용, 페이징 적용 */
    @Override
    @GetMapping
    public ResponseEntity<Page<AdminVacationListResponseDTO>> getVacationList(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) VacationApprove status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) VacationType type,
            @PageableDefault(size = 10, sort = "vacationStart", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));
        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        // 기본값: 오늘 기준 앞뒤로 3개월
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusMonths(3);
        }

        Page<AdminVacationListResponseDTO> response = vacationAdminService.getVacationList(startDate, endDate, status, name, type, pageable);
        return ResponseEntity.ok(response);
    }

    /** 휴가 상세 조회 (유형별 상세 정보 포함) - 관리자 전용 */
    @Override
    @GetMapping("/{vacationId}")
    public ResponseEntity<VacationDetailResponseDTO> getVacationDetail(
            @PathVariable Long vacationId,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));
        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        VacationDetailResponseDTO response = vacationAdminService.getVacationDetail(vacationId);
        return ResponseEntity.ok(response);
    }

    /** 휴가 통계 조회 (대기/승인/반려 건수) - 관리자 전용 */
    @Override
    @GetMapping("/statistics")
    public ResponseEntity<VacationStatisticsResponseDTO> getVacationStatistics(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));
        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        VacationStatisticsResponseDTO response = vacationAdminService.getVacationStatistics();
        return ResponseEntity.ok(response);
    }

    /** 휴가 승인 - 관리자 전용 */
    @Override
    @PatchMapping("/{vacationId}/approve")
    public ResponseEntity<Void> approveVacation(
            @PathVariable Long vacationId,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));
        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        vacationAdminService.approveVacation(vacationId);
        return ResponseEntity.ok().build();
    }

    /** 휴가 반려 (반려 사유 포함) - 관리자 전용 */
    @Override
    @PatchMapping("/{vacationId}/reject")
    public ResponseEntity<Void> rejectVacation(
            @PathVariable Long vacationId,
            @RequestBody VacationRejectRequestDTO request,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));
        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        vacationAdminService.rejectVacation(vacationId, request.getRejectReason());
        return ResponseEntity.ok().build();
    }
}
