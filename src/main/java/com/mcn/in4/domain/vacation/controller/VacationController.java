package com.mcn.in4.domain.vacation.controller;

import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationDetailResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationRemainderResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationResponseDTO;
import com.mcn.in4.domain.vacation.service.VacationService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * GET /api/vacations/my?memberId={memberId}
     */
    @GetMapping("/my")
    public ResponseEntity<List<VacationListResponseDTO>> getMyVacations(
            @RequestParam("memberId") Long memberId
    ) {
        List<VacationListResponseDTO> response = vacationService.getMyVacations(memberId);
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
