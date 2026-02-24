package com.mcn.in4.domain.creatorTodo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 크리에이터 Todo 아이템
 * - 매니저와 크리에이터가 공유하는 할 일
 * - 칸반 보드 형태로 관리 (할 일 → 진행 중 → 완료)
 */
@Entity
@Table(name = "creator_todo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long creatorId;   // 어떤 크리에이터의 보드인지

    @Column(nullable = false)
    private Long columnId;    // 어떤 컬럼에 속하는지

    @Column(nullable = false)
    private String content;   // 할 일 내용

    @Column(nullable = false)
    private Double position;  // 컬럼 내 순서 (드래그 정렬용)

    private Long createdBy;   // 작성자 memberId (매니저 or 크리에이터)

    private String createdByName; // 작성자 이름

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Version
    private Long version;     // 낙관적 락 (동시 수정 방지)

    public CreatorTodo(Long creatorId, Long columnId, String content, Double position,
                       Long createdBy, String createdByName) {
        this.creatorId = creatorId;
        this.columnId = columnId;
        this.content = content;
        this.position = position;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.createdAt = LocalDateTime.now();
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void move(Long targetColumnId, Double newPosition) {
        this.columnId = targetColumnId;
        this.position = newPosition;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePosition(Double newPosition) {
        this.position = newPosition;
        this.updatedAt = LocalDateTime.now();
    }
}
