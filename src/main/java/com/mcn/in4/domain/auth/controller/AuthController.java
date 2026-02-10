package com.mcn.in4.domain.auth.controller;

import com.mcn.in4.domain.auth.controller.api.AuthApi;
import com.mcn.in4.domain.auth.dto.request.AuthRequestDTO;
import com.mcn.in4.domain.auth.dto.response.AuthResponseDTO;
import com.mcn.in4.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO.loginRequestDto request) {
        return authService.login(request);
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            authService.logout(authentication.getName());
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @Override
    @PostMapping("/reissue")
    public AuthResponseDTO reissue(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        return authService.reissue(refreshToken);
    }
}
