package com.mcn.in4.domain.vacation.controller;

import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationDetailResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationRemainderResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationResponseDTO;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.vacation.service.VacationService;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 휴가 API 컨트롤러 (사용자용)
 * - POST /api/vacations?type={type} : 휴가 신청
 * - GET /api/vacations/my : 내 휴가 목록 조회
 * - GET /api/vacations/my/{vacationId} : 휴가 상세 조회
 * - GET /api/vacations/my/remainder : 잔여 연차 조회
 */
@RestController
@RequestMapping("/api/vacations")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    @PostMapping
    public ResponseEntity<VacationResponseDTO> createVacation(
            @RequestParam("type") String type,
            @RequestBody VacationRequestDTO request
    ) {
        VacationResponseDTO response = vacationService.createVacation(type, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 내 휴가 사용 내역 목록 조회
     * GET /api/vacations/my?memberId={memberId}&startDate=2025-12-28&endDate=2026-02-28&type=ANNUAL
     */
    @GetMapping("/my")
    public ResponseEntity<List<VacationListResponseDTO>> getMyVacations(
            @RequestParam("memberId") Long memberId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) VacationType type
    ) {
        // 기본값: 오늘 기준 앞뒤로 1개월
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusMonths(1);
        }

        List<VacationListResponseDTO> response = vacationService.getMyVacations(memberId, startDate, endDate, type);
        return ResponseEntity.ok(response);
    }

    /**
     * 휴가 상세 조회 (모달용)
     * GET /api/vacations/my/{vacationId}
     */
    @GetMapping("/my/{vacationId}")
    public ResponseEntity<VacationDetailResponseDTO> getVacationDetail(
            @PathVariable Long vacationId
    ) {
        VacationDetailResponseDTO response = vacationService.getVacationDetail(vacationId);
        return ResponseEntity.ok(response);
    }

    /**
     * 내 잔여 연차 조회
     * GET /api/vacations/my/remainder?memberId={memberId}
     */
    @GetMapping("/my/remainder")
    public ResponseEntity<VacationRemainderResponseDTO> getMyVacationRemainder(
            @RequestParam("memberId") Long memberId
    ) {
        VacationRemainderResponseDTO response = vacationService.getMyVacationRemainder(memberId);
        return ResponseEntity.ok(response);
    }
}
