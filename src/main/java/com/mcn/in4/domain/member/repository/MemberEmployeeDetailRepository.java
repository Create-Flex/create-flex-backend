package com.mcn.in4.domain.member.repository;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberEmployeeDetailRepository extends JpaRepository<MemberEmployeeDetail, Long> {
    Optional<MemberEmployeeDetail> findByMember(Member member);

    Optional<MemberEmployeeDetail> findByMemberMemberId(Long memberId);

    @Query("SELECT e.member.memberId FROM MemberEmployeeDetail e")
    List<Long> findEmployeeIds();
}