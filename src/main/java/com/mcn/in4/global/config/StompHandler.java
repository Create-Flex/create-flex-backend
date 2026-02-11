package com.mcn.in4.global.config;

import com.mcn.in4.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // CONNECT 요청일 때만 토큰 검증 및 인증 처리
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");

            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                jwtToken = jwtToken.substring(7);
            }
            // 토큰 유효성 검증
            if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken).isPresent()) {

                //  토큰에서 memberName 추출
                String memberName = jwtTokenProvider.getMemberNameFromToken(jwtToken);
                String role = jwtTokenProvider.getRoleFromToken(jwtToken);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                //  인증 객체 생성 (Principal 이름을 memberName으로 설정)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(memberName, null, Collections.singletonList(authority));

                // WebSocket 세션에 인증 정보 저장 -> ChatController에서 사용
                accessor.setUser(authentication);

                log.info("WebSocket 연결 성공: 사용자({})", memberName);
            } else {
                throw new RuntimeException("유효하지 않은 토큰입니다.");
            }
        }
        return message;
    }
}