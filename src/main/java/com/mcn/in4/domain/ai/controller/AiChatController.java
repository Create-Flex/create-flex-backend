package com.mcn.in4.domain.ai.controller;

import com.mcn.in4.domain.ai.dto.ChatRequest;
import com.mcn.in4.domain.ai.dto.ChatResponse;
import com.mcn.in4.domain.ai.service.AiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AI 챗봇 컨트롤러
 * AI 챗봇 관련 API를 제공합니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;

    /**
     * AI 챗봇에게 메시지를 보내고 응답을 받습니다.
     *
     * @param request 사용자 메시지를 담은 요청 DTO
     * @return AI의 응답을 담은 응답 DTO
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        log.info("AI Chat API 호출 - message: {}", request.getMessage());

        String reply = aiChatService.chat(request.getMessage());

        return ResponseEntity.ok(ChatResponse.builder()
                .reply(reply)
                .build());
    }
}
