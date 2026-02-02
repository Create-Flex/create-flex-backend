package com.mcn.in4.domain.health.repository;

import com.mcn.in4.domain.health.dto.MentalHealthDto;
import com.mcn.in4.domain.health.entity.CreatorMentalHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CreatorMentalHealthRepository extends JpaRepository<CreatorMentalHealth, Long> {

    @Query("SELECT new com.mcn.in4.domain.health.dto.MentalHealthDto(h.member.memberId, h.creatorMentalDate, h.creatorMentalScore) " +
            "FROM CreatorMentalHealth h " + // 엔티티 클래스명으로 변경
            "WHERE h.member.memberId IN :memberIds " +
            "AND h.creatorMentalDate = (" +
            "    SELECT MAX(h2.creatorMentalDate) " +
            "    FROM CreatorMentalHealth h2 " +
            "    WHERE h2.member.memberId = h.member.memberId" +
            ")")
    List<MentalHealthDto> findLatestMentalHealthByMemberIds(@Param("memberIds") List<Long> memberIds);
}
