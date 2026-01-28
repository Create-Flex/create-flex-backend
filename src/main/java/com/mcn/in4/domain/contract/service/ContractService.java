package com.mcn.in4.domain.contract.service;

import com.mcn.in4.domain.contract.dto.request.ContractRequestDTO;
import com.mcn.in4.domain.contract.dto.response.ContractResponseDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ContractService {

    // 계약 등록
    Long createContract(ContractRequestDTO.Create request);

    // 전체 계약 목록 조회
    List<ContractResponseDTO.Info> getAllContracts();

    /* 계약서 파일 직접 다운로드
    Resource downloadContractFile(Long contractId);

    // 계약서 파일명 가져오기
    String getContractFilename(Long contractId);

    // Pre-signed URL 생성 (임시 다운로드 링크)
    String generateContractDownloadUrl(Long contractId);
     */
}