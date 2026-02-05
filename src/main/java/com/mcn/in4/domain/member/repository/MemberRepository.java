package com.mcn.in4.domain.member.repository;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberAccount(String memberAccount);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.department " +
            "WHERE m.memberRole != 'CREATOR'")
    List<Member> findAllWithDepartment();

    List<Member> findByDepartment_DepartmentId(Long departmentId);

    List<Member> findByMemberIdIn(List<Long> memberIds);

    List<Member> findAllById(Iterable<Long> ids);

    long countByMemberRoleNot(MemberRole memberRole);

    List<Member> findByMemberNameContainingAndMemberIdIn(String memberName, List<Long> memberIds);

    // 매니저 목록 조회 (부서 정보 포함)
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.department " +
            "WHERE m.memberRole = :role AND m.memberStatus = :status")
    List<Member> findAllManagersWithDepartment(
            @Param("role") MemberRole role,
            @Param("status") MemberStatus status
    );

    Long countByDepartment_DepartmentId(Long departmentId);

    @Query("SELECT m.memberRole FROM Member m WHERE m.memberId = :memberId")
    MemberRole findMemberRoleByMemberId(@Param("memberId") Long memberId);
}
