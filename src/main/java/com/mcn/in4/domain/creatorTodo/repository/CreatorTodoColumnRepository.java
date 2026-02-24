package com.mcn.in4.domain.creatorTodo.repository;

import com.mcn.in4.domain.creatorTodo.entity.CreatorTodoColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreatorTodoColumnRepository extends JpaRepository<CreatorTodoColumn, Long> {

    // 크리에이터별 컬럼 목록 조회 (순서대로)
    List<CreatorTodoColumn> findByCreatorIdOrderBySequenceAsc(Long creatorId);

    // 해당 크리에이터의 컬럼 존재 여부 확인
    boolean existsByCreatorId(Long creatorId);
}
