package com.mcn.in4.domain.creator.repository;

import com.mcn.in4.domain.member.entity.MemberCreatorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreatorDetailRepository extends JpaRepository<MemberCreatorDetail, Long> {

        // 크리에이터 상세 정보 조회
        @Query("SELECT mcd FROM MemberCreatorDetail mcd " +
                        "JOIN FETCH mcd.memberCreator " +
                        "JOIN FETCH mcd.memberManager " +
                        "WHERE mcd.memberCreator.memberId = :creatorId")
        Optional<MemberCreatorDetail> findByCreatorIdWithManager(@Param("creatorId") Long creatorId);

        // 사용한 여러 크리에이터 상세 정보 조회
        @Query("SELECT mcd FROM MemberCreatorDetail mcd " +
                        "JOIN FETCH mcd.memberCreator " +
                        "JOIN FETCH mcd.memberManager " +
                        "WHERE mcd.memberCreator.memberId IN :creatorIds")
        List<MemberCreatorDetail> findByCreatorIdsWithManager(@Param("creatorIds") List<Long> creatorIds);

        // 크리에이터 ID로 크리에이터 상세 정보 삭제
        void deleteByMemberCreator_MemberId(Long creatorId);
}