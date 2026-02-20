package com.mcn.in4.global.config;

import com.mcn.in4.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
// @Order(Ordered.HIGHEST_PRECEDENCE + 99) // Security 인터셉터보다 먼저 실행되도록 우선순위 부여
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // CONNECT 요청일 때만 토큰 검증 및 인증 처리
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");

            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                jwtToken = jwtToken.substring(7);
            }

            if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken).isPresent()) {

                String memberName = jwtTokenProvider.getMemberNameFromToken(jwtToken);
                String role = jwtTokenProvider.getRoleFromToken(jwtToken);
                String memberId = jwtTokenProvider.getUserIdFromToken(jwtToken);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberId,
                        null, Collections.singletonList(authority));

                accessor.setUser(authentication);

                log.info(" STOMP CONNECT: MemberId={}, Name={}", memberId, memberName);
            } else {
                log.error(" 유효하지 않은 웹소켓 토큰입니다.");
                throw new RuntimeException("유효하지 않은 토큰입니다.");
            }
        }

        return message;
    }
}