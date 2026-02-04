package com.mcn.in4.domain.creator.service;

import com.mcn.in4.domain.creator.dto.request.CreatorWorkRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorWorkResponseDTO;

import java.util.List;

public interface CreatorWorkService {

    // 업무 목록 조회
    List<CreatorWorkResponseDTO.Info> getWorks(Long creatorId);

    // 업무 추가
    CreatorWorkResponseDTO.Info createWork(Long creatorId, Long workerId, CreatorWorkRequestDTO.Create request);

    // 업무 상태 변경
    CreatorWorkResponseDTO.Info updateWorkStatus(Long creatorId, Long workId, CreatorWorkRequestDTO.UpdateStatus request);

    // 업무 삭제
    void deleteWork(Long creatorId, Long workId);
}
