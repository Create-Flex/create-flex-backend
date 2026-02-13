package com.mcn.in4.global.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mcn.in4.domain.ai.tools.AttendanceTools;

/**
 * AI 공통 설정
 * ChatClient Bean을 생성합니다.
 * AI 챗봇(ai 도메인)과 이미지 분석(image 도메인) 공통으로 사용됩니다.
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
            AttendanceTools tools) {
        return builder
                // 도구는 AiChatServiceImpl에서 역할별로 동적 등록
                .build();
    }
}
