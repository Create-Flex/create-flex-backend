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

    @Query("SELECT e.member.memberId FROM MemberEmployeeDetail e WHERE e.member.memberStatus = com.mcn.in4.domain.member.entity.memberEnum.MemberStatus.WORKING")
    List<Long> findEmployeeIds();

    // 여러 회원의 상세 정보를 한 번에 조회 (IN 절)
    List<MemberEmployeeDetail> findByMemberMemberIdIn(List<Long> memberIds);
}