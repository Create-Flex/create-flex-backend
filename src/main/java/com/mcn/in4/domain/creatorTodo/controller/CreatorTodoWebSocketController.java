package com.mcn.in4.domain.creatorTodo.controller;

import com.mcn.in4.domain.creatorTodo.dto.TodoMoveRequest;
import com.mcn.in4.domain.creatorTodo.service.CreatorTodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CreatorTodoWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final CreatorTodoService todoService;

    /**
     * Todo 이동 (드래그앤드롭)
     * 클라이언트: /pub/todo/move
     * 구독: /sub/creator-todo/{creatorId}
     */
    @MessageMapping("/todo/move")
    public void moveTodo(@Payload TodoMoveRequest request) {
        log.info(">>> [Todo Move] ID: {}, TargetColumn: {}, Client: {}",
                request.getTodoId(),
                request.getTargetColumnId(),
                request.getClientUuid());

        try {
            // 1. 서비스 로직 수행 (DB 업데이트)
            Double newPosition = todoService.moveTodo(request);

            // 2. 응답 데이터 생성
            Map<String, Object> response = new HashMap<>();
            response.put("type", "MOVE_SUCCESS");
            response.put("todoId", request.getTodoId());
            response.put("columnId", request.getTargetColumnId());
            response.put("newPosition", newPosition);
            response.put("clientUuid", request.getClientUuid());

            // 3. 해당 크리에이터 보드 구독자에게 브로드캐스트
            String destination = "/sub/creator-todo/" + request.getCreatorId();
            messagingTemplate.convertAndSend(destination, response);
            log.debug(">>> 브로드캐스팅 완료: {}", destination);

        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn(">>> 동시성 충돌 발생! Client UUID: {}", request.getClientUuid());
            sendErrorResponse(request, "CONCURRENT_FAIL", "다른 사용자가 먼저 이동시켰습니다. 새로고침합니다.");
        } catch (Exception e) {
            log.error(">>> Todo 이동 중 에러 발생: ", e);
            sendErrorResponse(request, "ERROR", e.getMessage());
        }
    }

    /**
     * Todo 생성 알림 (다른 사용자에게 실시간 반영)
     * 클라이언트: /pub/todo/create
     */
    @MessageMapping("/todo/create")
    public void notifyTodoCreated(@Payload Map<String, Object> payload) {
        Long creatorId = Long.valueOf(payload.get("creatorId").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("type", "TODO_CREATED");
        response.putAll(payload);

        String destination = "/sub/creator-todo/" + creatorId;
        messagingTemplate.convertAndSend(destination, response);
        log.info(">>> Todo 생성 알림: creatorId={}", creatorId);
    }

    /**
     * Todo 삭제 알림
     * 클라이언트: /pub/todo/delete
     */
    @MessageMapping("/todo/delete")
    public void notifyTodoDeleted(@Payload Map<String, Object> payload) {
        Long creatorId = Long.valueOf(payload.get("creatorId").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("type", "TODO_DELETED");
        response.put("todoId", payload.get("todoId"));
        response.put("clientUuid", payload.get("clientUuid"));

        String destination = "/sub/creator-todo/" + creatorId;
        messagingTemplate.convertAndSend(destination, response);
        log.info(">>> Todo 삭제 알림: todoId={}", payload.get("todoId"));
    }

    /**
     * Todo 내용 수정 알림
     * 클라이언트: /pub/todo/update
     */
    @MessageMapping("/todo/update")
    public void notifyTodoUpdated(@Payload Map<String, Object> payload) {
        Long creatorId = Long.valueOf(payload.get("creatorId").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("type", "TODO_UPDATED");
        response.putAll(payload);

        String destination = "/sub/creator-todo/" + creatorId;
        messagingTemplate.convertAndSend(destination, response);
        log.info(">>> Todo 수정 알림: todoId={}", payload.get("todoId"));
    }

    private void sendErrorResponse(TodoMoveRequest request, String code, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("type", "ERROR");
        errorResponse.put("errorCode", code);
        errorResponse.put("errorMessage", message);
        errorResponse.put("clientUuid", request.getClientUuid());

        String destination = "/sub/creator-todo/" + request.getCreatorId();
        messagingTemplate.convertAndSend(destination, errorResponse);
    }
}
