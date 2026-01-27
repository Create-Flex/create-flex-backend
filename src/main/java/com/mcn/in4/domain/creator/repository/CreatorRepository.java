package com.mcn.in4.domain.creator.repository;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Member, Long> {

        // 크리에이터 전체 조회
        @Query("SELECT DISTINCT m FROM Member m " +
                        "LEFT JOIN FETCH m.department " +
                        "WHERE m.memberRole = :role " +
                        "AND m.memberStatus = :status")
        List<Member> findAllCreatorsWithDepartment(
                        @Param("role") MemberRole role,
                        @Param("status") MemberStatus status);

        // 크리에이터 단건 조회
        @Query("SELECT m FROM Member m " +
                        "LEFT JOIN FETCH m.department " +
                        "WHERE m.memberId = :memberId " +
                        "AND m.memberRole = :role " +
                        "AND m.memberStatus = :status")
        Optional<Member> findCreatorByIdWithDepartment(
                        @Param("memberId") Long memberId,
                        @Param("role") MemberRole role,
                        @Param("status") MemberStatus status);

        // 사번(계정) 중복 체크
        boolean existsByMemberAccount(String memberAccount);

        // 매니저별 크리에이터 조회
        @Query("SELECT DISTINCT m FROM Member m " +
                        "LEFT JOIN FETCH m.department " +
                        "JOIN MemberCreatorDetail mcd ON mcd.memberCreator = m " +
                        "WHERE mcd.memberManager.memberId = :managerId " +
                        "AND m.memberRole = :role " +
                        "AND m.memberStatus = :status")
        List<Member> findCreatorsByManagerIdWithDepartment(
                        @Param("managerId") Long managerId,
                        @Param("role") MemberRole role,
                        @Param("status") MemberStatus status);

        // 매니저 조회 (권한 확인용)
        @Query("SELECT m FROM Member m WHERE m.memberId = :managerId AND m.memberRole = :role")
        Optional<Member> findManagerById(@Param("managerId") Long managerId, @Param("role") MemberRole role);
}