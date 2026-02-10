package com.mcn.in4.domain.ai.service;

import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * AI 챗봇 서비스 구현체
 * Spring AI의 ChatClient를 사용하여 Ollama와 통신합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;

    @Override
    public String chat(String message) {
        log.info("AI Chat 요청: {}", message);

        try {
            String reply = chatClient.prompt()
                    .user(message)
                    .call()
                    .content();

            log.info("AI Chat 응답: {}", reply);
            return reply;
        } catch (Exception e) {
            log.error("AI Chat 에러: {}", e.getMessage(), e);

            // 연결 오류 판별
            if (isConnectionError(e)) {
                throw new CustomException(ErrorCode.AI_CONNECTION_FAILED,
                        "Ollama 서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요. (" + e.getMessage() + ")");
            }

            // 기타 AI 오류
            throw new CustomException(ErrorCode.AI_SERVICE_ERROR,
                    "AI 응답 생성 실패: " + e.getMessage());
        }
    }

    private boolean isConnectionError(Exception e) {
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        return msg.contains("connection refused")
                || msg.contains("connect timed out")
                || msg.contains("host")
                || e.getCause() instanceof java.net.ConnectException;
    }
}
