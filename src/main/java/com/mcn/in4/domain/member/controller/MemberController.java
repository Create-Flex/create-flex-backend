package com.mcn.in4.domain.member.controller;

import com.mcn.in4.domain.member.dto.MemberProfileResponseDto;
import com.mcn.in4.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberProfileResponseDto> getMemberProfile(@AuthenticationPrincipal String userId) {
        // JwtAuthenticationFilter에서 Principal로 String(userId)을 저장했으므로 String으로 받아야 함
        Long memberId = Long.parseLong(userId);
        MemberProfileResponseDto profileDto = memberService.getMemberProfile(memberId);
        return ResponseEntity.ok(profileDto);
    }
}
