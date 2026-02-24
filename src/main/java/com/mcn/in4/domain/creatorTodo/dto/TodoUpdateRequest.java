package com.mcn.in4.domain.creatorTodo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdateRequest {
    private Long todoId;
    private String content;
}
