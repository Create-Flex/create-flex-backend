package com.mcn.in4.global.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * JWT 인증 필터
 * - 모든 HTTP 요청에서 JWT 토큰을 검증하는 필터
 * - OncePerRequestFilter를 상속하여 요청당 한 번만 실행
 * - 유효한 토큰이면 SecurityContext에 인증 정보 저장
 * - 만료/무효한 토큰이면 401 응답 반환
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 필터 로직 구현
     * - Authorization 헤더에서 Bearer 토큰 추출
     * - 토큰 유효성 검증 후 SecurityContext에 인증 정보 설정
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. 요청정보에서 토큰 추출
            String token = getJwtFormRequest(request);

            // 2. 토큰이 정상적인 토큰인지를 검증
            if (StringUtils.hasText(token)) {
                Optional<Claims> claimsOpt = jwtTokenProvider.validateToken(token);

                if (claimsOpt.isPresent()) {
                    String userId = jwtTokenProvider.getUserIdFromToken(token);
                    String role = jwtTokenProvider.getRoleFromToken(token);

                    // 가져온 권한을 넣어주고
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                    // 유저의 id와 권한 같이 autentication에 넣어줌
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,
                            null, Collections.singletonList(authority));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 토큰이 있지만 유효하지 않음 (만료 등) -> 401 반환
                    logger.warn("토큰이 만료되었거나 유효하지 않습니다.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"message\": \"토큰이 만료되었습니다.\", \"code\": \"TOKEN_EXPIRED\"}");
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("JWT인증 에러", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\": \"인증 처리 중 오류가 발생했습니다.\", \"code\": \"AUTH_ERROR\"}");
            return;
        }

        filterChain.doFilter(request, response);

    }

    /**
     * HTTP 요청에서 JWT 토큰 추출
     * - Authorization 헤더에서 "Bearer " 접두사 제거 후 토큰 반환
     * @param request HTTP 요청 객체
     * @return JWT 토큰 문자열 (없으면 null)
     */
    private String getJwtFormRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // "Bearer " (7글자) 이후의 토큰 부분만 추출
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // SSE 연동을 위해 쿼리 파라미터 'token' 확인
        String tokenParam = request.getParameter("token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }

        return null;
    }
}
