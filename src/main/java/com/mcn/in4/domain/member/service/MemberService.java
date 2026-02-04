package com.mcn.in4.domain.member.service;

import com.mcn.in4.domain.member.dto.ManagerResponseDto;
import com.mcn.in4.domain.member.dto.MemberProfileResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberService {
    MemberProfileResponseDto getMemberProfile(Long memberId);
    MemberProfileResponseDto generatePresignedUrl(Long memberId, MultipartFile file);

    // 매니저 목록 조회
    List<ManagerResponseDto.ManagerInfo> getAllManagers();
}
