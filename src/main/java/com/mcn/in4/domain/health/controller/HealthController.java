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
/*
    @GetMapping("/manage")
    public List<HealthInfo> generateManageHealthInfo(

    )
*/
}