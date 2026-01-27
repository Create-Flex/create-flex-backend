package com.mcn.in4.domain.creator.repository;

import com.mcn.in4.entity.member.Member;
import com.mcn.in4.entity.member.memberEnum.MemberRole;
import com.mcn.in4.entity.member.memberEnum.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Member, Long> {

    // 크리에이터 역할과 상태로 조회
    List<Member> findByMemberRoleAndMemberStatus(MemberRole memberRole, MemberStatus memberStatus);

    // 크리에이터 ID와 상태로 조회
    Optional<Member> findByMemberIdAndMemberRoleAndMemberStatus(
            Long memberId, MemberRole memberRole, MemberStatus memberStatus);

    // 사번(계정) 중복 체크
    boolean existsByMemberAccount(String memberAccount);

    // 특정 매니저가 담당하는 크리에이터 목록 조회
    @Query("SELECT m FROM Member m " +
            "JOIN MemberCreatorDetail mcd ON mcd.memberCreator = m " +
            "WHERE mcd.memberManager.memberId = :managerId " +
            "AND m.memberRole = :role " +
            "AND m.memberStatus = :status")
    List<Member> findCreatorsByManagerId(
            @Param("managerId") Long managerId,
            @Param("role") MemberRole role,
            @Param("status") MemberStatus status);
}