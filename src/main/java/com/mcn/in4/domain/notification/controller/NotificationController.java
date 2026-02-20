package com.mcn.in4.domain.notification.controller;

import com.mcn.in4.domain.notification.service.NotificationService;
import com.mcn.in4.global.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

// 알림 컨트롤러
// SSE 연결 및 실시간 알림을 담당합니다.
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final SseEmitters sseEmitters;
    private final NotificationService notificationService;

    // 알림 목록 조회
    @GetMapping
    public java.util.List<com.mcn.in4.domain.notification.dto.NotificationDto> getNotifications(
            @AuthenticationPrincipal String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        return notificationService.getNotifications(Long.parseLong(userId));
    }

    // 알림 읽음 처리
    @org.springframework.web.bind.annotation.PatchMapping("/{notificationId}/read")
    public void markAsRead(@org.springframework.web.bind.annotation.PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    // 모든 알림 읽음 처리
    @org.springframework.web.bind.annotation.PatchMapping("/read-all")
    public void markAllAsRead(@AuthenticationPrincipal String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        notificationService.markAllAsRead(Long.parseLong(userId));
    }

    // SSE 연결 엔드포인트
    // 클라이언트가 이 엔드포인트에 연결하여 실시간 알림을 받습니다.
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required for notification subscription");
        }

        Long memberId = Long.parseLong(userId);

        // 타임아웃 설정 (30분)
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        // Emitter 등록
        sseEmitters.add(memberId, emitter);

        // 완료 시 Emitter 제거
        emitter.onCompletion(() -> sseEmitters.remove(memberId, emitter));

        // 타임아웃 시 Emitter 제거
        emitter.onTimeout(() -> sseEmitters.remove(memberId, emitter));

        // 에러 발생 시 Emitter 제거
        emitter.onError((e) -> sseEmitters.remove(memberId, emitter));

        // 연결 확인용 초기 메시지 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("SSE connection established"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}