package com.mcn.in4.domain.member.controller;

import com.mcn.in4.domain.member.dto.ManagerResponseDto;
import com.mcn.in4.domain.member.dto.MemberProfileResponseDto;
import com.mcn.in4.domain.member.service.MemberService;
import com.mcn.in4.domain.member.dto.MemberProfileRequestDto.ProfileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<MemberProfileResponseDto> generatePresignedUrl(
            @AuthenticationPrincipal String userId,
            ProfileUpload request){

        Long memberId = Long.parseLong(userId);
        MultipartFile file = request.getFile();
        MemberProfileResponseDto profileContaignPresign = memberService.generatePresignedUrl(memberId, file);
        return ResponseEntity.ok(profileContaignPresign);
    }

    @GetMapping("/managers")
    public ResponseEntity<List<ManagerResponseDto.ManagerInfo>> getAllManagers() {
        List<ManagerResponseDto.ManagerInfo> managers = memberService.getAllManagers();
        return ResponseEntity.ok(managers);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MemberProfileResponseDto> deleteProfile(
            @AuthenticationPrincipal String userId
    ){
        Long memberId = Long.parseLong(userId);
        MemberProfileResponseDto deleteProfile = memberService.deleteMemberProfile(memberId);
        return ResponseEntity.ok(deleteProfile);
    }

}