package com.mcn.in4.domain.creator.service;

import com.mcn.in4.domain.creator.dto.request.CreatorRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorResponseDTO;

import java.util.List;

public interface CreatorService {

    // 크리에이터 생성
    Long createCreator(CreatorRequestDTO.Create request);

    // 전체 크리에이터 조회
    List<CreatorResponseDTO.Info> getAllCreators();

    // 크리에이터 단건 조회
    CreatorResponseDTO.Info getCreatorById(Long creatorId);

    // 크리에이터 정보 수정
    CreatorResponseDTO.Info updateCreator(Long creatorId, CreatorRequestDTO.Update request);

    // 크리에이터 삭제 (SUSPENDED 상태로 변경)
    void deleteCreator(Long creatorId);

    // 매니저별 크리에이터 조회
    List<CreatorResponseDTO.Info> getMyCreators(Long managerId);
}