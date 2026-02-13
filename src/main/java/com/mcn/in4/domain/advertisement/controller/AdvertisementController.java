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
            @AuthenticationPrincipal String userId,
            @RequestBody AdvertisementRequestDTO.Create request) {

        Long currentUserId = Long.parseLong(userId);
        Long promotionId = advertisementService.createAdvertisement(request, currentUserId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "광고 캠페인이 성공적으로 등록되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 내 담당 크리에이터의 광고 캠페인 목록 조회 (매니저용)
    @Override
    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<AdvertisementResponseDTO.Info>> getMyAdvertisements(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false, defaultValue = "all") String filter,
            @org.springframework.data.web.PageableDefault(size = 8) org.springframework.data.domain.Pageable pageable) {

        Long managerId = Long.parseLong(userId);
        return ResponseEntity.ok(advertisementService.getMyAdvertisementsByFilter(managerId, filter, pageable));
    }

    // 광고 캠페인 수락, 거절(일정 자동 생성)
    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAdvertisementStatus(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id,
            @RequestParam String status) {

        Long currentUserId = Long.parseLong(userId);
        Map<String, Object> response = new HashMap<>();

        if ("ACCEPTED".equalsIgnoreCase(status)) {
            advertisementService.acceptAdvertisement(id, currentUserId);
            response.put("message", "광고 캠페인이 수락되었습니다.");
        } else if ("REJECTED".equalsIgnoreCase(status)) {
            advertisementService.rejectAdvertisement(id, currentUserId);
            response.put("message", "광고 캠페인이 거절되었습니다.");
        } else {
            throw new IllegalArgumentException("유효하지 않은 상태입니다. ACCEPTED 또는 REJECTED만 가능합니다.");
        }

        return ResponseEntity.ok(response);
    }
}