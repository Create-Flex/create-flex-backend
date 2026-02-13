package com.mcn.in4.domain.advertisement.service;

import com.mcn.in4.domain.advertisement.dto.request.AdvertisementRequestDTO;
import com.mcn.in4.domain.advertisement.dto.response.AdvertisementResponseDTO;

import java.util.List;

public interface AdvertisementService {

    // 광고 캠페인 등록
    Long createAdvertisement(AdvertisementRequestDTO.Create request, Long currentUserId);

    // 매니저가 담당하는 크리에이터들의 광고 캠페인 목록 조회 - 필터 추가
    org.springframework.data.domain.Page<AdvertisementResponseDTO.Info> getMyAdvertisementsByFilter(Long managerId,
            String filter, org.springframework.data.domain.Pageable pageable);

    // 광고 캠페인 수락 (일정 자동 생성)
    void acceptAdvertisement(Long promotionId, Long currentUserId);

    // 광고 캠페인 거절
    void rejectAdvertisement(Long promotionId, Long currentUserId);
}