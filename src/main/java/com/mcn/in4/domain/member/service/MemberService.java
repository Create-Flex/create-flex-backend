package com.mcn.in4.domain.member.service;

import com.mcn.in4.domain.member.dto.MemberProfileResponseDto;

public interface MemberService {
    MemberProfileResponseDto getMemberProfile(Long memberId);
}
