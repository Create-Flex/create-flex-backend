package com.mcn.in4.domain.creatorTodo.dto;

import com.mcn.in4.domain.creatorTodo.entity.CreatorTodo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorTodoDto {
    private Long id;
    private Long columnId;
    private String content;
    private Double position;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdAt;

    public static CreatorTodoDto from(CreatorTodo todo) {
        return CreatorTodoDto.builder()
                .id(todo.getId())
                .columnId(todo.getColumnId())
                .content(todo.getContent())
                .position(todo.getPosition())
                .createdBy(todo.getCreatedBy())
                .createdByName(todo.getCreatedByName())
                .createdAt(todo.getCreatedAt())
                .build();
    }
}
