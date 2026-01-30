package com.mcn.in4.domain.health.controller;

import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthInfo;
import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthPresigned;
import com.mcn.in4.domain.health.entity.CheckupSummanary;
import com.mcn.in4.domain.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/my")
    public List<HealthInfo> generateHealthInfo(Long memberId, LocalDate checkupDate, String checkupFileUrl){
        return healthService.generateHealthInfo(memberId, checkupDate, checkupFileUrl);
    }

    @PostMapping("/upload")
    public HealthPresigned generatePresignedUrl(Long memberId, String checkupName, LocalDate date,  CheckupSummanary checkupSummanary, MultipartFile file){
        return healthService.generatePresignedUrl(memberId, checkupName, date, checkupSummanary, file);
    }
}