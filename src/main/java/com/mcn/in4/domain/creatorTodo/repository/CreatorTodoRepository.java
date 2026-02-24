package com.mcn.in4.domain.creatorTodo.repository;

import com.mcn.in4.domain.creatorTodo.entity.CreatorTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreatorTodoRepository extends JpaRepository<CreatorTodo, Long> {

    // 크리에이터의 모든 Todo 조회 (position 순)
    List<CreatorTodo> findByCreatorIdOrderByColumnIdAscPositionAsc(Long creatorId);

    // 특정 컬럼의 Todo 목록 조회 (position 순)
    List<CreatorTodo> findByColumnIdOrderByPositionAsc(Long columnId);

    // 컬럼 내 최대 position 조회 (새 Todo 추가 시 사용)
    @Query("SELECT MAX(t.position) FROM CreatorTodo t WHERE t.columnId = :columnId")
    Optional<Double> findMaxPositionByColumnId(@Param("columnId") Long columnId);

    // 크리에이터의 특정 컬럼 Todo 목록 조회
    List<CreatorTodo> findByCreatorIdAndColumnIdOrderByPositionAsc(Long creatorId, Long columnId);
}
