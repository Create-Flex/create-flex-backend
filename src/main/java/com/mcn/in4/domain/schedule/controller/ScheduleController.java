package com.mcn.in4.domain.schedule.controller;

import com.mcn.in4.domain.schedule.dto.resquestDTO.ScheduleRequestDTO;
import com.mcn.in4.domain.schedule.service.SchedulService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final SchedulService schedulService;

    @PostMapping("/")
    public ResponseEntity<String> createSchedule(
            @AuthenticationPrincipal String userId,
            Authentication authentication, // 토큰 에서 정보
            @RequestBody ScheduleRequestDTO.ScheduleCreateRequestDto requestDto) {

        Long memberId = Long.parseLong(userId);

        // 토큰에서 권한 추출
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        schedulService.createSchedule(memberId, role, requestDto);

        return ResponseEntity.ok("일정이 성공적으로 등록되었습니다.");
    }


}
