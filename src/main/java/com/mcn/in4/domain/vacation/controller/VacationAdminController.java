package com.mcn.in4.domain.vacation.controller;

import com.mcn.in4.domain.vacation.dto.request.VacationRejectRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.service.VacationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 휴가 관리 API 컨트롤러 (관리자용)
 * - GET /api/admin/vacations : 전체 휴가 목록 조회 (필터: 기간, 상태, 이름)
 * - PATCH /api/admin/vacations/{id}/approve : 휴가 승인
 * - PATCH /api/admin/vacations/{id}/reject : 휴가 반려
 */
@RestController
@RequestMapping("/api/admin/vacations")
@RequiredArgsConstructor
public class VacationAdminController {

    private final VacationAdminService vacationAdminService;

    /**
     * 전체 휴가 신청 목록 조회 (관리자용)
     * GET /api/admin/vacations?startDate=2026-01-01&endDate=2026-01-31&status=APPROVE_NEED&name=홍길동
     */
    @GetMapping
    public ResponseEntity<List<AdminVacationListResponseDTO>> getVacationList(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) VacationApprove status,
            @RequestParam(required = false) String name
    ) {
        // 기본값: 오늘 기준 한 달
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<AdminVacationListResponseDTO> response = vacationAdminService.getVacationList(startDate, endDate, status, name);
        return ResponseEntity.ok(response);
    }

    /**
     * 휴가 승인
     * PATCH /api/admin/vacations/{vacationId}/approve
     */
    @PatchMapping("/{vacationId}/approve")
    public ResponseEntity<Void> approveVacation(@PathVariable Long vacationId) {
        vacationAdminService.approveVacation(vacationId);
        return ResponseEntity.ok().build();
    }

    /**
     * 휴가 반려
     * PATCH /api/admin/vacations/{vacationId}/reject
     */
    @PatchMapping("/{vacationId}/reject")
    public ResponseEntity<Void> rejectVacation(
            @PathVariable Long vacationId,
            @RequestBody VacationRejectRequestDTO request
    ) {
        vacationAdminService.rejectVacation(vacationId, request.getRejectReason());
        return ResponseEntity.ok().build();
    }
}
