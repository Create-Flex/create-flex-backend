package com.mcn.in4.domain.creatorTodo.service;

import com.mcn.in4.domain.creatorTodo.dto.*;
import com.mcn.in4.domain.creatorTodo.entity.CreatorTodo;
import com.mcn.in4.domain.creatorTodo.entity.CreatorTodoColumn;
import com.mcn.in4.domain.creatorTodo.repository.CreatorTodoColumnRepository;
import com.mcn.in4.domain.creatorTodo.repository.CreatorTodoRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorTodoService {

    private final CreatorTodoRepository todoRepository;
    private final CreatorTodoColumnRepository columnRepository;
    private final MemberRepository memberRepository;

    /**
     * 크리에이터용 기본 칸반 보드 초기화 (첫 접근 시)
     * 할 일, 진행 중, 완료 3개 컬럼 생성
     */
    @Transactional
    public void initializeBoard(Long creatorId) {
        if (columnRepository.existsByCreatorId(creatorId)) {
            return; // 이미 초기화됨
        }

        columnRepository.save(new CreatorTodoColumn(creatorId, "할 일", 1));
        columnRepository.save(new CreatorTodoColumn(creatorId, "진행 중", 2));
        columnRepository.save(new CreatorTodoColumn(creatorId, "완료", 3));

        log.info("크리에이터 {} 칸반 보드 초기화 완료", creatorId);
    }

    /**
     * 칸반 보드 전체 조회
     */
    public List<CreatorTodoColumnDto> getBoard(Long creatorId) {
        // 컬럼 목록 조회
        List<CreatorTodoColumn> columns = columnRepository.findByCreatorIdOrderBySequenceAsc(creatorId);

        // Todo 목록 조회 후 컬럼별로 그룹핑
        List<CreatorTodo> allTodos = todoRepository.findByCreatorIdOrderByColumnIdAscPositionAsc(creatorId);
        Map<Long, List<CreatorTodoDto>> todosByColumn = allTodos.stream()
                .map(CreatorTodoDto::from)
                .collect(Collectors.groupingBy(CreatorTodoDto::getColumnId));

        // 컬럼 + Todo 조합
        return columns.stream()
                .map(column -> CreatorTodoColumnDto.from(
                        column,
                        todosByColumn.getOrDefault(column.getId(), List.of())
                ))
                .collect(Collectors.toList());
    }

    /**
     * Todo 생성
     */
    @Transactional
    public CreatorTodoDto createTodo(TodoCreateRequest request, Long memberId) {
        // 작성자 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 해당 컬럼의 마지막 position + 1
        Double maxPosition = todoRepository.findMaxPositionByColumnId(request.getColumnId())
                .orElse(0.0);

        CreatorTodo todo = new CreatorTodo(
                request.getCreatorId(),
                request.getColumnId(),
                request.getContent(),
                maxPosition + 1.0,
                memberId,
                member.getMemberName()
        );

        todoRepository.save(todo);
        log.info("Todo 생성: creatorId={}, content={}", request.getCreatorId(), request.getContent());

        return CreatorTodoDto.from(todo);
    }

    /**
     * Todo 내용 수정
     */
    @Transactional
    public CreatorTodoDto updateTodo(TodoUpdateRequest request) {
        CreatorTodo todo = todoRepository.findById(request.getTodoId())
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        todo.updateContent(request.getContent());
        return CreatorTodoDto.from(todo);
    }

    /**
     * Todo 이동 (드래그앤드롭)
     * @return 새 position 값
     */
    @Transactional
    public Double moveTodo(TodoMoveRequest request) {
        if (request.getTodoId() == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        CreatorTodo todo = todoRepository.findById(request.getTodoId())
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        Double newPosition = request.getNewPosition();

        // position이 지정되지 않은 경우 컬럼 맨 끝에 추가
        if (newPosition == null) {
            newPosition = todoRepository.findMaxPositionByColumnId(request.getTargetColumnId())
                    .orElse(0.0) + 1.0;
        }

        todo.move(request.getTargetColumnId(), newPosition);

        log.info("Todo 이동: id={}, targetColumn={}, newPosition={}",
                request.getTodoId(), request.getTargetColumnId(), newPosition);

        return newPosition;
    }

    /**
     * Todo 삭제
     */
    @Transactional
    public void deleteTodo(Long todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new CustomException(ErrorCode.TODO_NOT_FOUND);
        }
        todoRepository.deleteById(todoId);
        log.info("Todo 삭제: id={}", todoId);
    }
}
