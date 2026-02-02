package com.mcn.in4.domain.advertisement.controller;

import com.mcn.in4.domain.advertisement.controller.api.AdvertisementApi;
import com.mcn.in4.domain.advertisement.dto.request.AdvertisementRequestDTO;
import com.mcn.in4.domain.advertisement.dto.response.AdvertisementResponseDTO;
import com.mcn.in4.domain.advertisement.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
public class AdvertisementController implements AdvertisementApi {

    private final AdvertisementService advertisementService;


    // 광고 캠페인 등록
    @Override
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAdvertisement(
            @RequestBody AdvertisementRequestDTO.Create request) {

        Long promotionId = advertisementService.createAdvertisement(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "광고 캠페인이 성공적으로 등록되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 내 담당 크리에이터의 광고 캠페인 목록 조회 (매니저용)
    // GET /api/advertisements?filter=all (전체보기 - 기본값)
    // GET /api/advertisements?filter=waiting (대기중인 제안)
    // GET /api/advertisements?filter=processed (처리내역)
    @Override
    @GetMapping
    public ResponseEntity<List<AdvertisementResponseDTO.Info>> getMyAdvertisements(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false, defaultValue = "all") String filter) {

        Long managerId = Long.parseLong(userId);
        return ResponseEntity.ok(advertisementService.getMyAdvertisementsByFilter(managerId, filter));
    }

    // 광고 캠페인 수락, 거절 (일정 자동 생성)
    // PATCH /api/advertisements/{id}?status=ACCEPTED (수락)
    // PATCH /api/advertisements/{id}?status=REJECTED (거절)
    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAdvertisementStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Map<String, Object> response = new HashMap<>();

        if ("ACCEPTED".equalsIgnoreCase(status)) {
            advertisementService.acceptAdvertisement(id);
            response.put("message", "광고 캠페인이 수락되었으며, 일정이 자동으로 추가되었습니다.");
        } else if ("REJECTED".equalsIgnoreCase(status)) {
            advertisementService.rejectAdvertisement(id);
            response.put("message", "광고 캠페인이 거절되었습니다.");
        } else {
            throw new IllegalArgumentException("유효하지 않은 상태입니다. ACCEPTED 또는 REJECTED만 가능합니다.");
        }

        return ResponseEntity.ok(response);
    }
}