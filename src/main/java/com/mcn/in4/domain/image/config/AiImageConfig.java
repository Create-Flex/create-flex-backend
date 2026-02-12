package com.mcn.in4.domain.image.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AiImageConfig {
    /**
     * 이미지 도메인에서 사용할 OpenAI 모델을 Primary로 설정하여
     * 다른 AI 도메인(Ollama 등)과의 빈 충돌을 방지합니다.
     */
    @Bean
    @Primary
    public ChatModel openAiChatModelForImage(OpenAiChatModel openAiChatModel) {
        return openAiChatModel;
    }
}