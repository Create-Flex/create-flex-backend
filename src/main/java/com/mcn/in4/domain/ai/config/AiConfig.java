package com.mcn.in4.domain.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mcn.in4.domain.ai.tools.AttendanceTools;

/**
 * AI 관련 설정
 * ChatClient Bean을 생성합니다.
 */
@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
            AttendanceTools tools) {
        return builder
                // .defaultSystem(...) - AiChatServiceImpl에서 동적으로 설정함
                .defaultFunctions("getMyAttendanceSummary", "getAllAttendanceSummary") // 함수 등록
                .build();
    }
}
