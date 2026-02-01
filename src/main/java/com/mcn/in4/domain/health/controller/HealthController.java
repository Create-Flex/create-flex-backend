package com.mcn.in4.domain.health.controller;

import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthInfo;
import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthPresigned;
import com.mcn.in4.domain.health.dto.HealthRequestDto.HealthUpload;
import com.mcn.in4.domain.health.entity.CheckupSummanary;
import com.mcn.in4.domain.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    public List<HealthInfo> generateHealthInfo(
            @RequestParam Long memberId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate){
        return healthService.generateHealthInfo(memberId, startDate, endDate);
    }

    @GetMapping("/creater")
    public List<HealthInfo> generateCreatorHealthInfo(
            @RequestParam Long memberId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate){
        return healthService.generateCreatorHealthInfo(memberId, startDate, endDate);
    }

    @GetMapping("/manage")
    public List<HealthInfo> generateManageHealthInfo(

    )

    @PostMapping("/upload")
    @ResponseBody
    public HealthPresigned generatePresignedUrl(HealthUpload request){
        Long memberId = request.getMemberId();
        String checkupName = request.getName();
        LocalDate date = request.getDate();
        CheckupSummanary checkupSummanary = request.getSummanary();
        MultipartFile file = request.getFile();
        return healthService.generatePresignedUrl(memberId, checkupName, date, checkupSummanary, file);
    }
}