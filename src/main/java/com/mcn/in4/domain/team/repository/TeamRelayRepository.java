package com.mcn.in4.domain.team.repository;

import com.mcn.in4.domain.team.entity.TeamRelay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TeamRelayRepository extends JpaRepository<TeamRelay, Long> {

    // N+1 문제 방지를 위해 Member와 Department를 조인해서 가져옵니다.
    @Query("select tr from TeamRelay tr " +
            "join fetch tr.member m " +
            "left join fetch m.department " +
            "where tr.team.teamId = :teamId")
    List<TeamRelay> findAllByTeamIdWithMemberAndDept(@Param("teamId") Long teamId);

    // 팀 ID에 해당하는 모든 멤버 연결 데이터를 삭제합니다.
    @Modifying
    @Query("delete from TeamRelay tr where tr.team.teamId = :teamId")
    void deleteAllByTeamId(@Param("teamId") Long teamId);

    List<TeamRelay> findAllByMemberMemberId(Long memberId);
    
}