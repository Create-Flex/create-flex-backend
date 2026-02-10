package com.mcn.in4.domain.creator.service;

import com.mcn.in4.domain.creator.dto.request.CreatorRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CreatorService {

    // 크리에이터 생성
    Long createCreator(CreatorRequestDTO.Create request);

    // 전체 크리에이터 조회
    Page<CreatorResponseDTO.Info> getAllCreators(String name, Pageable pageable);

    // 크리에이터 상세 조회
    CreatorResponseDTO.Info getCreatorById(Long creatorId);

    // 크리에이터 정보 수정
    CreatorResponseDTO.Info updateCreator(Long creatorId, CreatorRequestDTO.Update request);

    // 크리에이터 삭제
    void deleteCreator(Long creatorId);

    // 매니저별 크리에이터 조회
    List<CreatorResponseDTO.Info> getMyCreators(Long managerId);
}