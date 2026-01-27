package com.mcn.in4.domain.creator.repository;

import com.mcn.in4.entity.member.MemberCreatorDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorDetailRepository extends JpaRepository<MemberCreatorDetail, Long> {

    // 크리에이터 ID로 크리에이터 상세 정보 조회
    Optional<MemberCreatorDetail> findByMemberCreator_MemberId(Long creatorId);

    // 크리에이터 ID로 크리에이터 상세 정보 삭제
    void deleteByMemberCreator_MemberId(Long creatorId);
}