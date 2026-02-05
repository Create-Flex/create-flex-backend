package com.mcn.in4.domain.health.service;

import com.mcn.in4.domain.health.dto.HealthResponseDto;
import com.mcn.in4.domain.health.dto.HealthResponseDto.*;
import com.mcn.in4.domain.health.entity.CheckupSummanary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface HealthService {
    MypageHealthInfo generateMypageHealthInfo(Long memberId, LocalDate startDate, LocalDate endDate);
    HealthResponseDto.CreatorHealthInfo generateCreatorHealthInfo(Long memberId);
    HealthPresigned generatePresignedUrl(Long memberId, String checkupName, LocalDate date, CheckupSummanary checkupSummanary, MultipartFile file);
    List<HealthInfo> findByNameAndPeriod(String name, LocalDate startDate, LocalDate endDate);
    List<HealthInfo> findByName(String name);
    List<HealthInfo> findByPeriod(LocalDate startDate, LocalDate endDate);
    List<HealthInfo> findAll();
    List<HealthInfo> generateManageHealthInfo();
    void saveMentalHealthTest(Long memberId, Long score);
}
