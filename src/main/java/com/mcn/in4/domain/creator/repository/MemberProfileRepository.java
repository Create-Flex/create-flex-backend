package com.mcn.in4.domain.creator.repository;

import com.mcn.in4.entity.member.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    // 회원 ID로 프로필 조회
    Optional<MemberProfile> findByMember_MemberId(Long memberId);
}