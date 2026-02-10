package com.mcn.in4.domain.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 관련 설정
 * ChatClient Bean을 생성합니다.
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("당신은 CreatorFlex 서비스의 AI 어시스턴트입니다. 한국어로 친절하게 답변해주세요.")
                .build();
    }
}
