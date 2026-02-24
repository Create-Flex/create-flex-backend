package com.mcn.in4.domain.creatorTodo.dto;

import com.mcn.in4.domain.creatorTodo.entity.CreatorTodoColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorTodoColumnDto {
    private Long id;
    private String title;
    private Integer sequence;
    private List<CreatorTodoDto> todos;

    public static CreatorTodoColumnDto from(CreatorTodoColumn column, List<CreatorTodoDto> todos) {
        return CreatorTodoColumnDto.builder()
                .id(column.getId())
                .title(column.getTitle())
                .sequence(column.getSequence())
                .todos(todos)
                .build();
    }
}
