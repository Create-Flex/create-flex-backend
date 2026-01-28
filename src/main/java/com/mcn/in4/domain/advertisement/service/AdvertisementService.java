package com.mcn.in4.domain.advertisement.service;

import com.mcn.in4.domain.advertisement.dto.request.AdvertisementRequestDTO;
import com.mcn.in4.domain.advertisement.dto.response.AdvertisementResponseDTO;

import java.util.List;

public interface AdvertisementService {

    // 광고 캠페인 등록
    Long createAdvertisement(AdvertisementRequestDTO.Create request);

    // 전체 광고 캠페인 목록 조회
    List<AdvertisementResponseDTO.Info> getAllAdvertisements();

    // 상태별 광고 캠페인 목록 조회
    List<AdvertisementResponseDTO.Info> getAdvertisementsByStatus(String status);

    // 광고 캠페인 수락 (일정 자동 생성)
    void acceptAdvertisement(Long promotionId);

    // 광고 캠페인 거절
    void rejectAdvertisement(Long promotionId);
}