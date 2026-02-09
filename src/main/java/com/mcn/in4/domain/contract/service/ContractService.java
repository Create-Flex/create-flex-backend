package com.mcn.in4.domain.contract.service;

import com.mcn.in4.domain.contract.dto.request.ContractRequestDTO;
import com.mcn.in4.domain.contract.dto.response.ContractResponseDTO;

import java.util.List;

public interface ContractService {

    // 계약 등록 (파일 업로드)
    ContractResponseDTO.Create createContract(ContractRequestDTO.Create request);

    // 전체 계약 목록 조회
    List<ContractResponseDTO.Info> getAllContracts();
}