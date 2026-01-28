package com.mcn.in4.domain.advertisement.repository;

import com.mcn.in4.domain.advertisement.entity.CreatorPromotion;
import com.mcn.in4.domain.creator.entity.creatorEnum.PromotionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<CreatorPromotion, Long> {

    // 전체 광고 캠페인 목록 조회 (크리에이터 정보 함께 조회, 최신순)
    @Query("SELECT DISTINCT cp FROM CreatorPromotion cp " +
            "JOIN FETCH cp.memberCreator " +
            "ORDER BY cp.createdAt DESC")
    List<CreatorPromotion> findAllWithCreator();

    // 상태별 광고 캠페인 목록 조회
    @Query("SELECT DISTINCT cp FROM CreatorPromotion cp " +
            "JOIN FETCH cp.memberCreator " +
            "WHERE cp.promotionStatus = :status " +
            "ORDER BY cp.createdAt DESC")
    List<CreatorPromotion> findByStatus(@Param("status") PromotionStatus status);

    // 광고 캠페인 단건 조회 (크리에이터 정보 함께 조회)
    @Query("SELECT cp FROM CreatorPromotion cp " +
            "JOIN FETCH cp.memberCreator " +
            "WHERE cp.promotionId = :promotionId")
    Optional<CreatorPromotion> findByIdWithCreator(@Param("promotionId") Long promotionId);
}