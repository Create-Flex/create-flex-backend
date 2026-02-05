package com.mcn.in4.domain.health.controller.api;

import com.mcn.in4.domain.health.dto.HealthResponseDto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Health", description = "건강 관리 API")
public interface CreatorHealthApi {
    @Operation(
            summary = "크리에이터 건강 정보 생성",
            description = "memberId를 기준으로 해당 크리에이터의 최신 건강 정보를 생성하여 반환합니다."
    )
    CreatorHealthInfo generateCreatorHealthInfo(
            @AuthenticationPrincipal String userId
    );

    @Operation(
            summary = "정신 건강 검사 결과 저장",
            description = "로그인한 사용자의 PHQ-9 정신 건강 검사 점수를 저장하고, 생성된 검사 기록의 ID를 반환합니다."
    )
    @PostMapping("/upload/mental")
    ResponseEntity<Void> saveMentalHealthTest(
            @AuthenticationPrincipal String userId,
            @RequestParam Long score
    );
}
