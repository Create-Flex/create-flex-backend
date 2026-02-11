package com.mcn.in4.global.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SslOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정 클래스
 * - Refresh Token 저장을 위한 Redis 연결 설정
 * - Lettuce 클라이언트 사용 (비동기 지원, 높은 성능)
 * - Redis Cloud 또는 로컬 Redis 서버 연결 지원
 * - SSL 연결 옵션 지원
 */
@Configuration
public class RedisConfig {

    /** Redis 서버 호스트 주소 */
    @Value("${spring.data.redis.host}")
    private String host;

    /** Redis 서버 포트 번호 */
    @Value("${spring.data.redis.port}")
    private int port;

    /** Redis 서버 비밀번호 */
    @Value("${spring.data.redis.password}")
    private String password;

    /** SSL 연결 사용 여부 (기본값: true) */
    @Value("${spring.data.redis.ssl.enabled:true}")
    private boolean sslEnabled;

    /**
     * Redis 연결 팩토리 설정
     * - Lettuce 클라이언트를 사용하여 Redis 서버에 연결
     * - SSL 옵션에 따라 보안 연결 설정
     * @return RedisConnectionFactory 인스턴스
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(password);

        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder =
                LettuceClientConfiguration.builder();

        if (sslEnabled) {
            builder.useSsl();
        }

        return new LettuceConnectionFactory(config, builder.build());
    }

    /**
     * RedisTemplate 설정
     * - Key/Value 모두 String 타입으로 직렬화
     * - Refresh Token 저장/조회/삭제에 사용
     * - StringRedisSerializer 사용으로 Redis CLI에서 직접 확인 가능
     * @param connectionFactory Redis 연결 팩토리
     * @return 설정된 RedisTemplate 인스턴스
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
