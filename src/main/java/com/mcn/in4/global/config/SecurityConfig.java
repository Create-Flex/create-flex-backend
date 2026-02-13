package com.mcn.in4.global.config;

import com.mcn.in4.global.security.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 설정 클래스
 * - JWT 기반 인증을 위한 Stateless 세션 정책 설정
 * - CSRF 비활성화 (REST API는 토큰 기반 인증이므로 CSRF 공격에 안전)
 * - CORS 설정 (프론트엔드 localhost:3000 허용)
 * - URL별 권한 설정 (permitAll, hasRole, hasAnyRole)
 * - JwtAuthenticationFilter를 필터 체인에 추가하여 모든 요청에서 JWT 검증
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Security Filter Chain 설정
     * - 모든 HTTP 요청에 대한 보안 규칙 정의
     * - JWT 필터를 UsernamePasswordAuthenticationFilter 전에 추가
     * 
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
                        .requestMatchers("/error").permitAll()
                        // Swagger 권한
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers("/ws-stomp/**").permitAll()
                        // 인증없이 가능한 경우
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/reissue").permitAll()
                        // .requestMatchers(HttpMethod.POST,"/**").permitAll()

                        // 관리자 전용
                        // 크리에이터 관리
                        .requestMatchers(HttpMethod.POST, "/api/creators").hasRole("ADMINISTRATOR")
                        // 담당하는 크리에이터 조회
                        .requestMatchers(HttpMethod.GET, "/api/creators/my").hasAnyRole("MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/creators/{creatorId}")
                        .hasAnyRole("ADMINISTRATOR", "CREATOR", "MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/creators/{creatorId}").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/creators/{creatorId}").hasRole("ADMINISTRATOR")

                        // 계약 관리
                        .requestMatchers(HttpMethod.GET, "/api/contracts").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/api/contracts").hasRole("ADMINISTRATOR")

                        // 법률 세무 관리
                        .requestMatchers(HttpMethod.GET, "/api/legaltax/admin/all").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/legaltax/{legalTaxId}/complete")
                        .hasRole("ADMINISTRATOR")

                        // 직원 마이페이지 (CREATOR 제외)
                        .requestMatchers(HttpMethod.PATCH, "/api/employees/me")
                        .hasAnyRole("EMPLOYEE", "MANAGER", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/employees/password")
                        .hasAnyRole("EMPLOYEE", "MANAGER", "ADMINISTRATOR")

                        // 나머지경로
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 설정
     * - 허용 Origin: http://localhost:3000 (프론트엔드)
     * - 허용 메서드: GET, POST, PUT, DELETE, OPTIONS, PATCH
     * - 인증정보(Authorization 헤더) 포함 요청 허용
     * - Preflight 요청 캐시: 1시간
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true); // 인증정보를 포함한 cors요청 허용
        corsConfiguration.setMaxAge(3600L); // 1시간

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 비밀번호 암호화 인코더 설정
     * - BCrypt 해시 알고리즘 사용 (단방향 암호화)
     * - 로그인 시 비밀번호 검증에 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
