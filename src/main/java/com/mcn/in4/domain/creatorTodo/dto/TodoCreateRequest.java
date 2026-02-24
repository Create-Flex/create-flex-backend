package com.mcn.in4.domain.creatorTodo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoCreateRequest {
    private Long creatorId;
    private Long columnId;
    private String content;
}
