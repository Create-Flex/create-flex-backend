package com.mcn.in4.domain.creator.repository;

import com.mcn.in4.domain.creator.entity.CreatorWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreatorWorkRepository extends JpaRepository<CreatorWork, Long> {

    // 크리에이터별 업무 목록 조회 (작성자 이름 포함)
    @Query("SELECT cw FROM CreatorWork cw " +
            "JOIN FETCH cw.memberWorker " +
            "WHERE cw.memberCreator.memberId = :creatorId")
    List<CreatorWork> findByCreatorIdWithWorker(@Param("creatorId") Long creatorId);

    // 특정 크리에이터의 특정 업무 조회
    @Query("SELECT cw FROM CreatorWork cw " +
            "JOIN FETCH cw.memberWorker " +
            "WHERE cw.creatorWorkId = :workId " +
            "AND cw.memberCreator.memberId = :creatorId")
    Optional<CreatorWork> findByWorkIdAndCreatorId(@Param("workId") Long workId, @Param("creatorId") Long creatorId);
}
