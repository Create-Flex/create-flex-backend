package com.mcn.in4.domain.creatorTodo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 크리에이터 Todo 컬럼 (칸반 보드 컬럼)
 * - 할 일, 진행 중, 완료
 * - 크리에이터별로 고정된 3개 컬럼
 */
@Entity
@Table(name = "creator_todo_column")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorTodoColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long creatorId;  // 어떤 크리에이터의 보드인지

    @Column(nullable = false)
    private String title;    // 컬럼 이름 (할 일, 진행 중, 완료)

    @Column(nullable = false)
    private Integer sequence; // 컬럼 순서 (0, 1, 2)

    public CreatorTodoColumn(Long creatorId, String title, Integer sequence) {
        this.creatorId = creatorId;
        this.title = title;
        this.sequence = sequence;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
