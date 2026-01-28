package com.mcn.in4.domain.legalTax.repository;

import com.mcn.in4.domain.legalTax.entity.CreatorLegalTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegalTaxRepository extends JpaRepository<CreatorLegalTax, Long> {

    // 전체 상담 신청 목록 조회 (최신순)
    @Query("SELECT DISTINCT lt FROM CreatorLegalTax lt " +
            "JOIN FETCH lt.memberCreator " +
            "ORDER BY lt.legalTaxId DESC")
    List<CreatorLegalTax> findAllWithCreator();

    // 특정 크리에이터의 상담 신청 목록 조회
    @Query("SELECT lt FROM CreatorLegalTax lt " +
            "JOIN FETCH lt.memberCreator " +
            "WHERE lt.memberCreator.memberId = :creatorId " +
            "ORDER BY lt.legalTaxId DESC")
    List<CreatorLegalTax> findByCreatorId(@Param("creatorId") Long creatorId);

    // 매니저가 담당하는 크리에이터들의 상담 신청 목록 조회
    @Query("SELECT DISTINCT lt FROM CreatorLegalTax lt " +
            "JOIN FETCH lt.memberCreator mc " +
            "JOIN MemberCreatorDetail mcd ON mcd.memberCreator = mc " +
            "WHERE mcd.memberManager.memberId = :managerId " +
            "ORDER BY lt.legalTaxId DESC")
    List<CreatorLegalTax> findByManagerId(@Param("managerId") Long managerId);
}