package com.mcn.in4.domain.member.repository;

import com.mcn.in4.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
