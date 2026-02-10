package com.mcn.in4.domain.ai.service;

/**
 * AI 챗봇 서비스 인터페이스
 * Ollama를 통한 AI 채팅 기능을 제공합니다.
 */
public interface AiChatService {

    /**
     * 사용자 메시지에 대한 AI 응답을 생성합니다.
     *
     * @param message 사용자의 질문 또는 메시지
     * @return AI의 응답 텍스트
     */
    String chat(String message);
}
