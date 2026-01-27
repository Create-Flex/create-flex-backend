package com.mcn.in4.entity.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberEmployeeDetailRepository extends JpaRepository<MemberEmployeeDetail, Long> {
    Optional<MemberEmployeeDetail> findByMember(Member member);
    Optional<MemberEmployeeDetail> findByMemberMemberId(Long memberId);
}
