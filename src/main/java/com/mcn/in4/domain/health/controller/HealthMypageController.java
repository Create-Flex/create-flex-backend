package com.mcn.in4.domain.health.controller;

import com.mcn.in4.domain.health.dto.HealthRequestDto;
import com.mcn.in4.domain.health.dto.HealthResponseDto;
import com.mcn.in4.domain.health.entity.CheckupSummanary;
import com.mcn.in4.domain.health.service.HealthService;
import com.mcn.in4.domain.health.controller.api.MypageHealthApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/health/my")
@RequiredArgsConstructor
public class HealthMypageController implements MypageHealthApi {

    private final HealthService healthService;

    @GetMapping("/")
    public List<HealthResponseDto.HealthInfo> generateHealthInfo(
            @RequestParam Long memberId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate){
        return healthService.generateHealthInfo(memberId, startDate, endDate);
    }

    @PostMapping("/upload")
    @ResponseBody
    public HealthResponseDto.HealthPresigned generatePresignedUrl(HealthRequestDto.HealthUpload request){
        Long memberId = request.getMemberId();
        String checkupName = request.getName();
        LocalDate date = request.getDate();
        CheckupSummanary checkupSummanary = request.getSummanary();
        MultipartFile file = request.getFile();
        return healthService.generatePresignedUrl(memberId, checkupName, date, checkupSummanary, file);
    }
}
