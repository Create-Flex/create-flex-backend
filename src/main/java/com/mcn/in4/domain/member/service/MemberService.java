package com.mcn.in4.domain.member.service;

import com.mcn.in4.domain.member.dto.MemberProfileResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    MemberProfileResponseDto getMemberProfile(Long memberId);
    MemberProfileResponseDto generatePresignedUrl(Long memberId, MultipartFile file);
}
