package com.mcn.in4.domain.team.repository;

import com.mcn.in4.domain.team.entity.TeamRelay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TeamRelayRepository extends JpaRepository<TeamRelay, Long> {

    // N+1 문제 방지를 위해 Member와 Department를 조인해서 가져옵니다.
    @Query("select tr from TeamRelay tr " +
            "join fetch tr.member m " +
            "left join fetch m.department " +
            "where tr.team.teamId = :teamId")
    List<TeamRelay> findAllByTeamIdWithMemberAndDept(@Param("teamId") Long teamId);
}