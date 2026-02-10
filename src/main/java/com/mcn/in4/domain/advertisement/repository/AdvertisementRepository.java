package com.mcn.in4.domain.advertisement.repository;

import com.mcn.in4.domain.advertisement.entity.CreatorPromotion;
import com.mcn.in4.domain.creator.entity.creatorEnum.PromotionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<CreatorPromotion, Long> {

        // 매니저가 담당하는 크리에이터들의 전체 광고 캠페인 목록 조회
        @Query("SELECT DISTINCT cp FROM CreatorPromotion cp " +
                        "JOIN FETCH cp.memberCreator mc " +
                        "JOIN MemberCreatorDetail mcd ON mcd.memberCreator = mc " +
                        "WHERE mcd.memberManager.memberId = :managerId " +
                        "ORDER BY cp.createdAt DESC")
        org.springframework.data.domain.Page<CreatorPromotion> findByManagerId(@Param("managerId") Long managerId,
                        org.springframework.data.domain.Pageable pageable);

        // 매니저가 담당하는 크리에이터들의 광고 캠페인 중 특정 상태 조회
        @Query("SELECT DISTINCT cp FROM CreatorPromotion cp " +
                        "JOIN FETCH cp.memberCreator mc " +
                        "JOIN MemberCreatorDetail mcd ON mcd.memberCreator = mc " +
                        "WHERE mcd.memberManager.memberId = :managerId " +
                        "AND cp.promotionStatus = :status " +
                        "ORDER BY cp.createdAt DESC")
        org.springframework.data.domain.Page<CreatorPromotion> findByManagerIdAndStatus(
                        @Param("managerId") Long managerId,
                        @Param("status") PromotionStatus status,
                        org.springframework.data.domain.Pageable pageable);

        // 매니저가 담당하는 크리에이터들의 광고 캠페인 중 처리내역 조회 (ACCEPTED + REJECTED)
        @Query("SELECT DISTINCT cp FROM CreatorPromotion cp " +
                        "JOIN FETCH cp.memberCreator mc " +
                        "JOIN MemberCreatorDetail mcd ON mcd.memberCreator = mc " +
                        "WHERE mcd.memberManager.memberId = :managerId " +
                        "AND cp.promotionStatus IN ('ACCEPTED', 'REJECTED') " +
                        "ORDER BY cp.createdAt DESC")
        Page<CreatorPromotion> findByManagerIdAndProcessedStatus(
                        @Param("managerId") Long managerId, Pageable pageable);

        // 광고 캠페인 단건 조회 (크리에이터 정보 함께 조회)
        @Query("SELECT cp FROM CreatorPromotion cp " +
                        "JOIN FETCH cp.memberCreator " +
                        "WHERE cp.promotionId = :promotionId")
        Optional<CreatorPromotion> findByIdWithCreator(@Param("promotionId") Long promotionId);
}