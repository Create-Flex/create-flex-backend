package com.mcn.in4.domain.health.controller;

import com.mcn.in4.domain.health.dto.HealthResponseDto.CreatorHealthInfo;
import com.mcn.in4.domain.health.service.HealthService;
import com.mcn.in4.domain.health.controller.api.CreatorHealthApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/health/creator")
@RequiredArgsConstructor
public class HealthCreatorController implements CreatorHealthApi {
    private final HealthService healthService;

    @GetMapping("/")
    public CreatorHealthInfo generateCreatorHealthInfo(
            @AuthenticationPrincipal String userId) {
        Long memberId = Long.parseLong(userId);
        return healthService.generateCreatorHealthInfo(memberId);
    }

    @PostMapping("/upload/mental")
    public ResponseEntity<Void> saveMentalHealthTest(
            @AuthenticationPrincipal String userId,
            @RequestParam Long score){
        Long memberId = Long.parseLong(userId);
        healthService.saveMentalHealthTest(memberId, score);
        return ResponseEntity.ok().build();
    }
}