package com.mcn.in4.domain.member.repository;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    // 회원 ID로 프로필 조회
    Optional<MemberProfile> findByMember_MemberId(Long memberId);

    // 여러 회원 ID로 프로필 일괄 조회
    @Query("SELECT mp FROM MemberProfile mp " +
            "WHERE mp.member.memberId IN :memberIds")
    List<MemberProfile> findByMemberIds(@Param("memberIds") List<Long> memberIds);
    
    Optional<MemberProfile> findByMember(Member member);
}