package com.mcn.in4.domain.creatorTodo.controller;

import com.mcn.in4.domain.creatorTodo.dto.*;
import com.mcn.in4.domain.creatorTodo.service.CreatorTodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creator-todo")
@RequiredArgsConstructor
public class CreatorTodoController {

    private final CreatorTodoService todoService;

    /**
     * 칸반 보드 조회 (컬럼 + Todo 목록)
     * 첫 접근 시 자동으로 기본 컬럼 생성
     */
    @GetMapping("/{creatorId}")
    public ResponseEntity<List<CreatorTodoColumnDto>> getBoard(@PathVariable Long creatorId) {
        // 보드가 없으면 초기화
        todoService.initializeBoard(creatorId);
        return ResponseEntity.ok(todoService.getBoard(creatorId));
    }

    /**
     * Todo 생성
     */
    @PostMapping
    public ResponseEntity<CreatorTodoDto> createTodo(
            @RequestBody TodoCreateRequest request,
            @AuthenticationPrincipal String userId) {
        Long memberId = Long.parseLong(userId);
        return ResponseEntity.ok(todoService.createTodo(request, memberId));
    }

    /**
     * Todo 내용 수정
     */
    @PutMapping
    public ResponseEntity<CreatorTodoDto> updateTodo(@RequestBody TodoUpdateRequest request) {
        return ResponseEntity.ok(todoService.updateTodo(request));
    }

    /**
     * Todo 삭제
     */
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.noContent().build();
    }
}
