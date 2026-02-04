package com.mcn.in4.global.config;

import com.mcn.in4.global.security.JwtAuthenticationFilter;
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
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                       // Swagger 권한
                       .requestMatchers(
                               "/swagger-ui/**",
                               "/v3/api-docs/**",
                               "/swagger-ui.html"
                       ).permitAll()

                        //인증없이 가능한 경우
                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
//                        .requestMatchers(HttpMethod.POST,"/**").permitAll()

                          // 관리자 전용
                          // 크리에이터 관리
                          .requestMatchers(HttpMethod.GET, "/api/creators").hasRole("ADMINISTRATOR")
                          .requestMatchers(HttpMethod.POST, "/api/creators").hasRole("ADMINISTRATOR")
                          // 담당하는 크리에이터 조회
                          .requestMatchers(HttpMethod.GET, "/api/creators/my").hasAnyRole("MANAGER", "ADMINISTRATOR")
                          .requestMatchers(HttpMethod.GET, "/api/creators/{creatorId}").hasRole("ADMINISTRATOR")
                          .requestMatchers(HttpMethod.PATCH, "/api/creators/{creatorId}").hasRole("ADMINISTRATOR")
                          .requestMatchers(HttpMethod.DELETE, "/api/creators/{creatorId}").hasRole("ADMINISTRATOR")

                          // 계약 관리
                          .requestMatchers(HttpMethod.GET, "/api/contracts").hasRole("ADMINISTRATOR")
                          .requestMatchers(HttpMethod.POST, "/api/contracts").hasRole("ADMINISTRATOR")

                          // 법률 세무 관리
                          .requestMatchers(HttpMethod.GET, "/api/legaltax/admin/all").hasRole("ADMINISTRATOR")
                          .requestMatchers(HttpMethod.PATCH, "/api/legaltax/{legalTaxId}/complete").hasRole("ADMINISTRATOR")


                        //나머지경로
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true); //인증정보를 포함한 cors요청 허용
        corsConfiguration.setMaxAge(3600L); //1시간

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
