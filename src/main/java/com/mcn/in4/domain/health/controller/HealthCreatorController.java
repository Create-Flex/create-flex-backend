package com.mcn.in4.domain.health.controller;

import com.mcn.in4.domain.health.dto.HealthResponseDto.CreatorHealthInfo;
import com.mcn.in4.domain.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/health/creator")
@RequiredArgsConstructor
public class HealthCreatorController {
    private final HealthService healthService;

    @GetMapping("/")
    public CreatorHealthInfo generateCreatorHealthInfo(
            @RequestParam Long memberId) {
        return healthService.generateCreatorHealthInfo(memberId);
    }
}
