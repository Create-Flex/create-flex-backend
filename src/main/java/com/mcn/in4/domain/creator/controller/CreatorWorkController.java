package com.mcn.in4.domain.creator.controller;

import com.mcn.in4.domain.creator.controller.api.CreatorWorkApi;
import com.mcn.in4.domain.creator.dto.request.CreatorWorkRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorWorkResponseDTO;
import com.mcn.in4.domain.creator.service.CreatorWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/creators/{creatorId}/works")
@RequiredArgsConstructor
public class CreatorWorkController implements CreatorWorkApi {

    private final CreatorWorkService creatorWorkService;

    // 업무 목록 조회
    @Override
    @GetMapping
    public ResponseEntity<List<CreatorWorkResponseDTO.Info>> getWorks(@PathVariable Long creatorId) {
        return ResponseEntity.ok(creatorWorkService.getWorks(creatorId));
    }

    // 업무 추가
    @Override
    @PostMapping
    public ResponseEntity<CreatorWorkResponseDTO.Info> createWork(
            @PathVariable Long creatorId,
            @RequestBody CreatorWorkRequestDTO.Create request,
            @AuthenticationPrincipal String userId) {
        Long workerId = Long.parseLong(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(creatorWorkService.createWork(creatorId, workerId, request));
    }

    // 업무 상태 변경
    @Override
    @PatchMapping("/{workId}")
    public ResponseEntity<CreatorWorkResponseDTO.Info> updateWorkStatus(
            @PathVariable Long creatorId,
            @PathVariable Long workId,
            @RequestBody CreatorWorkRequestDTO.UpdateStatus request) {
        return ResponseEntity.ok(creatorWorkService.updateWorkStatus(creatorId, workId, request));
    }

    // 업무 삭제
    @Override
    @DeleteMapping("/{workId}")
    public ResponseEntity<Map<String, Object>> deleteWork(
            @PathVariable Long creatorId,
            @PathVariable Long workId) {
        creatorWorkService.deleteWork(creatorId, workId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "업무가 성공적으로 삭제되었습니다.");

        return ResponseEntity.ok(response);
    }
}
