package com.mcn.in4.domain.auth.controller.api;

import com.mcn.in4.domain.auth.dto.request.AuthRequestDTO;
import com.mcn.in4.domain.auth.dto.response.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증(Auth) 관리", description = "로그인 및 로그아웃을 담당하는 API입니다.")
public interface AuthApi {

    @Operation(
            summary = "로그인",
            description = "사번과 비밀번호를 사용하여 JWT 토큰을 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공 및 토킹 발급"),
                    @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치")
            }
    )
    AuthResponseDTO login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "로그인 정보",
                    content = @Content(examples = @ExampleObject(value = "{\n  \"memberAccount\": \"HR001\",\n  \"password\": \"admin123!\"\n}"))
            ) AuthRequestDTO.loginRequestDto request
    );

    @Operation(
            summary = "로그아웃",
            description = "현재 로그인된 세션을 종료합니다. (토큰 무효화)",
            security = @SecurityRequirement(name = "JWT")
    )
    ResponseEntity<String> logout();
}