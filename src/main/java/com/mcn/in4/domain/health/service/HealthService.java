package com.mcn.in4.domain.health.service;

import com.mcn.in4.domain.health.dto.HealthResponseDto;
import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthPresigned;
import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthInfo;
import com.mcn.in4.domain.health.entity.CheckupSummanary;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface HealthService {
    List<HealthInfo> generateHealthInfo(Long memberId, LocalDate startDate, LocalDate endDate);
    HealthResponseDto.CreatorHealthInfo generateCreatorHealthInfo(Long memberId);
    HealthPresigned generatePresignedUrl(Long memberId, String checkupName, LocalDate date, CheckupSummanary checkupSummanary, MultipartFile file);
}
