package com.mcn.in4.domain.creatorTodo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoMoveRequest {
    private Long todoId;
    private Long targetColumnId;
    private Double newPosition;     // 드래그 후 새 위치
    private Long creatorId;         // 어느 크리에이터 보드인지
    private String clientUuid;      // 클라이언트 식별 (낙관적 UI용)
}
