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
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //요청정보를 가지고 인증후, 인증이 완료되면 정보를 security에 저장

        try {
            //1. 요청정보에서 토큰 추출
            String token = getJwtFormRequest(request);

            //2. 토큰이 정상적인 토큰인지를 검증
            if (StringUtils.hasText(token)) {
                Optional<Claims> claimsOpt = jwtTokenProvider.validateToken(token);

                if (claimsOpt.isPresent()) {
                    String userId = jwtTokenProvider.getUserIdFromToken(token);
                    String role = jwtTokenProvider.getRoleFromToken(token);

                    //가져온 권한을 넣어주고
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                    // 유저의 id와 권한 같이 autentication에 넣어줌
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(authority));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("JWT인증 에러", e);
        }

        filterChain.doFilter(request, response);

    }

    private String getJwtFormRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) &&  bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
