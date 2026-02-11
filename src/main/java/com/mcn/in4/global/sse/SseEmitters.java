package com.mcn.in4.global.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// SSE Emitter 관리 컴포넌트
// 사용자별 SSE 연결을 관리하고 이벤트를 전송합니다.
@Component
public class SseEmitters {

    // 사용자별 Emitter 리스트 (memberId -> List<SseEmitter>)
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    // 새로운 Emitter를 추가합니다.
    public void add(Long memberId, SseEmitter emitter) {
        emitters.computeIfAbsent(memberId, k -> new CopyOnWriteArrayList<>()).add(emitter);
    }

    // Emitter를 제거합니다.
    public void remove(Long memberId, SseEmitter emitter) {
        List<SseEmitter> memberEmitters = emitters.get(memberId);
        if (memberEmitters != null) {
            memberEmitters.remove(emitter);
            if (memberEmitters.isEmpty()) {
                emitters.remove(memberId);
            }
        }
    }

    // 특정 사용자에게 이벤트를 전송합니다.
    public void send(Long memberId, String eventName, Object data) {
        List<SseEmitter> memberEmitters = emitters.get(memberId);

        if (memberEmitters != null && !memberEmitters.isEmpty()) {
            List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();

            memberEmitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name(eventName)
                            .data(data));
                } catch (IOException e) {
                    deadEmitters.add(emitter);
                }
            });

            // 죽은 Emitter 제거
            deadEmitters.forEach(emitter -> remove(memberId, emitter));
        }
    }

    // 여러 사용자에게 동일한 이벤트를 전송합니다.
    public void sendToMembers(List<Long> memberIds, String eventName, Object data) {
        memberIds.forEach(memberId -> send(memberId, eventName, data));
    }

    // 모든 연결된 사용자에게 이벤트를 전송합니다.
    public void sendToAll(String eventName, Object data) {
        emitters.keySet().forEach(memberId -> send(memberId, eventName, data));
    }
}