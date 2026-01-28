package com.mcn.in4.domain.contract.repository;

import com.mcn.in4.domain.contract.entity.CreatorContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<CreatorContract, Long> {

    // 계약 목록 조회 (크리에이터 정보 함께 조회)
    @Query("SELECT DISTINCT c FROM CreatorContract c " +
            "ORDER BY c.contractStart DESC")
    List<CreatorContract> findAllContractsWithCreator();
}