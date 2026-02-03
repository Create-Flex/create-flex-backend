package com.mcn.in4.domain.health.controller.api;

import com.mcn.in4.domain.health.dto.HealthResponseDto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Health", description = "건강 관리 API")
public interface CreatorHealthApi {
    @Operation(
            summary = "크리에이터 건강 정보 생성",
            description = "memberId를 기준으로 해당 크리에이터의 최신 건강 정보를 생성하여 반환합니다."
    )
    CreatorHealthInfo generateCreatorHealthInfo(
            @Parameter(description = "크리에이터 회원 ID", example = "1003", required = true)
            @RequestParam Long memberId
    );
}
