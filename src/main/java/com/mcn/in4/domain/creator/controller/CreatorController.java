package com.mcn.in4.domain.creator.controller;

import com.mcn.in4.domain.creator.dto.request.CreatorRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorResponseDTO;
import com.mcn.in4.domain.creator.service.CreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/creators")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService creatorService;

    // 크리에이터 등록
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCreator(@RequestBody CreatorRequestDTO.Create request) {
        Long creatorId = creatorService.createCreator(request);
        log.info(request.toString());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "크리에이터가 성공적으로 등록되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 크리에이터 목록 조회
    // GET /api/creators (전체 조회)
    // GET /api/creators?name=감
    @GetMapping
    public ResponseEntity<List<CreatorResponseDTO.Info>> getAllCreators(
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(creatorService.getAllCreators(name));
    }

    // 크리에이터 상세 조회
    @GetMapping("/{creatorId}")
    public ResponseEntity<CreatorResponseDTO.Info> getCreatorById(@PathVariable Long creatorId) {
        return ResponseEntity.ok(creatorService.getCreatorById(creatorId));
    }

    // 크리에이터 수정
    @PatchMapping("/{creatorId}")
    public ResponseEntity<Map<String, Object>> updateCreator(
            @PathVariable Long creatorId,
            @RequestBody CreatorRequestDTO.Update request) {

        CreatorResponseDTO.Info updatedCreator = creatorService.updateCreator(creatorId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "크리에이터가 성공적으로 수정되었습니다.");

        return ResponseEntity.ok(response);
    }

    // 크리에이터 삭제
    @DeleteMapping("/{creatorId}")
    public ResponseEntity<Map<String, Object>> deleteCreator(@PathVariable Long creatorId) {
        creatorService.deleteCreator(creatorId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "크리에이터가 성공적으로 삭제되었습니다.");

        return ResponseEntity.ok(response);
    }

    // 매니저별 크리에이터 조회
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<CreatorResponseDTO.Info>> getMyCreators(@PathVariable Long managerId) {
        return ResponseEntity.ok(creatorService.getMyCreators(managerId));
    }
}