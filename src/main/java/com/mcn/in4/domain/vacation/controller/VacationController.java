package com.mcn.in4.domain.vacation.controller;

import com.mcn.in4.domain.vacation.controller.api.VacationApi;
import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.MyVacationStatsResponseDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class VacationController implements VacationApi {

    private final VacationService vacationService;

    /** 휴가 신청 (연차, 반차, 경조사, 병가, 워케이션) */
    @Override
    @PostMapping
    public ResponseEntity<VacationResponseDTO> createVacation(
            @AuthenticationPrincipal String userId,
            @RequestParam("type") String type,
            @RequestBody VacationRequestDTO request,
            Authentication authentication
    ) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));
        if (isCreator) {
            return ResponseEntity.status(403).build();
        }

        Long memberId = Long.parseLong(userId);
        VacationResponseDTO response = vacationService.createVacation(type, request, memberId);
        return ResponseEntity.ok(response);
    }

    /** 내 휴가 사용 내역 목록 조회 (기간, 휴가유형 필터) */
    @Override
    @GetMapping("/my")
    public ResponseEntity<List<VacationListResponseDTO>> getMyVacations(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) VacationType type,
            Authentication authentication
    ) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));
        if (isCreator) {
            return ResponseEntity.status(403).build();
        }

        Long memberId = Long.parseLong(userId);

        // 기본값: 오늘 기준 앞뒤로 3개월
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusMonths(3);
        }

        List<VacationListResponseDTO> response = vacationService.getMyVacations(memberId, startDate, endDate, type);
        return ResponseEntity.ok(response);
    }

    /** 휴가 상세 조회 (본인 휴가 또는 관리자만 조회 가능) */
    @Override
    @GetMapping("/my/{vacationId}")
    public ResponseEntity<VacationDetailResponseDTO> getVacationDetail(
            @AuthenticationPrincipal String userId,
            @PathVariable Long vacationId,
            Authentication authentication
    ) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));
        if (isCreator) {
            return ResponseEntity.status(403).build();
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));

        Long memberId = Long.parseLong(userId);
        VacationDetailResponseDTO response = vacationService.getVacationDetail(vacationId, memberId, isAdmin);
        return ResponseEntity.ok(response);
    }

    /** 내 잔여 연차 조회 */
    @Override
    @GetMapping("/my/remainder")
    public ResponseEntity<VacationRemainderResponseDTO> getMyVacationRemainder(
            @AuthenticationPrincipal String userId,
            Authentication authentication
    ) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));
        if (isCreator) {
            return ResponseEntity.status(403).build();
        }

        Long memberId = Long.parseLong(userId);
        VacationRemainderResponseDTO response = vacationService.getMyVacationRemainder(memberId);
        return ResponseEntity.ok(response);
    }

    /** 내 휴가 통계 조회 (승인된 휴가 수, 미승인 휴가 수) */
    @Override
    @GetMapping("/my/stats")
    public ResponseEntity<MyVacationStatsResponseDTO> getMyVacationStats(
            @AuthenticationPrincipal String userId,
            Authentication authentication
    ) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));
        if (isCreator) {
            return ResponseEntity.status(403).build();
        }

        Long memberId = Long.parseLong(userId);
        MyVacationStatsResponseDTO response = vacationService.getMyVacationStats(memberId);
        return ResponseEntity.ok(response);
    }
}
