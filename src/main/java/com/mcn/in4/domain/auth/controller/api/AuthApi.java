package com.mcn.in4.domain.auth.controller.api;

import com.mcn.in4.domain.auth.dto.request.AuthRequestDTO;
import com.mcn.in4.domain.auth.dto.response.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


@Tag(name = "인증(Auth) 관리", description = "로그인 및 로그아웃을 담당하는 API입니다.")
public interface AuthApi {

    @Operation(
            summary = "로그인",
            description = "사번과 비밀번호를 사용하여 JWT 토큰을 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공 및 토큰 발급"),
                    @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호 불일치")
            }
    )
    AuthResponseDTO login(
            @RequestBody(
                    description = "로그인 정보",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "관리자 로그인",
                                            summary = "관리자 계정",
                                            description = "HR 부서 관리자로 로그인하는 예시입니다.",
                                            value = """
                                                    {
                                                      "memberAccount": "HR001",
                                                      "password": "admin123!"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "매니저 로그인",
                                            summary = "매니저 계정",
                                            description = "크리에이터 매니저로 로그인하는 예시입니다.",
                                            value = """
                                                    {
                                                      "memberAccount": "MG001",
                                                      "password": "manager123"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "크리에이터 로그인",
                                            summary = "크리에이터 계정",
                                            description = "크리에이터로 로그인하는 예시입니다.",
                                            value = """
                                                    {
                                                      "memberAccount": "gamst",
                                                      "password": "gam12345"
                                                    }
                                                    """
                                    )
                            }
                    )
            ) AuthRequestDTO.loginRequestDto request
    );

    @Operation(
            summary = "로그아웃",
            description = "현재 로그인된 세션을 종료합니다. (토큰 무효화)",
            security = @SecurityRequirement(name = "JWT")
    )
    ResponseEntity<String> logout();
}