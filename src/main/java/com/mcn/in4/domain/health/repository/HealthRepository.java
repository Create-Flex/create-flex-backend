package com.mcn.in4.domain.health.repository;

import com.mcn.in4.domain.health.dto.HealthResponseDto;
import com.mcn.in4.domain.health.dto.HealthSummanaryCountDto;
import com.mcn.in4.domain.health.entity.Health;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {
    List<Health> findByMember_MemberIdAndCheckupDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.mcn.in4.domain.health.dto.HealthSummanaryCountDto(h.checkupSummanary, COUNT(h)) " +
            "FROM Health h " +
            "WHERE h.member.memberId IN :memberIds " +
            "GROUP BY h.checkupSummanary " +
            "ORDER BY COUNT(h) DESC")
    List<HealthSummanaryCountDto> countGroupedByCheckupSummanaryForMembers(
            @Param("memberIds") List<Long> memberIds);

    Optional<Health> findTopByMember_MemberId(Long memberId);
}
