package com.mcn.in4.domain.legalTax.repository;

import com.mcn.in4.domain.legalTax.entity.CreatorLegalTax;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxStatus;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegalTaxRepository extends JpaRepository<CreatorLegalTax, Long> {

        // 전체 상담 신청 목록 조회 (최신순)
        // 전체 상담 신청 목록 조회 (최신순)
        @Query("SELECT DISTINCT lt FROM CreatorLegalTax lt " +
                        "JOIN FETCH lt.memberCreator " +
                        "WHERE (:type IS NULL OR lt.legalTaxType = :type) " +
                        "AND (:status IS NULL OR lt.legalTaxStatus = :status) " +
                        "AND (:excludeDone IS FALSE OR lt.legalTaxStatus != 'DONE') " +
                        "ORDER BY lt.legalTaxId DESC")
        Page<CreatorLegalTax> findAllWithFilters(
                        @Param("type") LegalTaxType type,
                        @Param("status") LegalTaxStatus status,
                        @Param("excludeDone") boolean excludeDone,
                        Pageable pageable);

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
                        "AND (:type IS NULL OR lt.legalTaxType = :type) " +
                        "AND (:status IS NULL OR lt.legalTaxStatus = :status) " +
                        "AND (:excludeDone IS FALSE OR lt.legalTaxStatus != 'DONE') " +
                        "ORDER BY lt.legalTaxId DESC")
        Page<CreatorLegalTax> findByManagerIdWithFilters(
                        @Param("managerId") Long managerId,
                        @Param("type") LegalTaxType type,
                        @Param("status") LegalTaxStatus status,
                        @Param("excludeDone") boolean excludeDone,
                        Pageable pageable);
}