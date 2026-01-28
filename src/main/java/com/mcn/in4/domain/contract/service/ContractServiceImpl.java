package com.mcn.in4.domain.contract.service;

import com.mcn.in4.domain.contract.dto.request.ContractRequestDTO;
import com.mcn.in4.domain.contract.dto.response.ContractResponseDTO;
import com.mcn.in4.domain.contract.entity.CreatorContract;
import com.mcn.in4.domain.contract.repository.ContractRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long createContract(ContractRequestDTO.Create request) {
        // 1. 크리에이터 존재 여부 확인
        Member creator = memberRepository.findById(request.getMemberCreatorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 크리에이터입니다: " + request.getMemberCreatorId()));

        // 2. 크리에이터 권한 확인
        if (creator.getMemberRole() != MemberRole.CREATOR) {
            throw new IllegalArgumentException("크리에이터만 계약을 등록할 수 있습니다.");
        }

        // 3. 계약 기간 검증
        if (request.getContractStart().isAfter(request.getContractEnd())) {
            throw new IllegalArgumentException("계약 시작일은 종료일보다 이전이어야 합니다.");
        }

        // 4. 계약 엔티티 생성 및 저장
        CreatorContract contract = CreatorContract.builder()
                .memberCreator(creator)
                .contractName(request.getContractName())
                .contractStart(request.getContractStart())
                .contractEnd(request.getContractEnd())
                .contractFileUrl(request.getContractFileUrl())
                .build();

        CreatorContract savedContract = contractRepository.save(contract);

        return savedContract.getCreatorContractId();
    }

    @Override
    public List<ContractResponseDTO.Info> getAllContracts() {
        List<CreatorContract> contracts = contractRepository.findAllContractsWithCreator();

        return contracts.stream()
                .map(ContractResponseDTO.Info::from)
                .collect(Collectors.toList());
    }

    /* S3 구현후 사용
    @Override
    public String generateContractDownloadUrl(Long contractId) {
        CreatorContract contract = findContract(contractId);
        validateFileExists(contract);

        // 1시간 유효한 Pre-signed URL 생성
        return s3Service.generatePresignedUrl(
                contract.getContractFileUrl(),
                Duration.ofHours(1)
        );
    }

    private CreatorContract findContract(Long contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 계약입니다: " + contractId));
    }

    private void validateFileExists(CreatorContract contract) {
        if (contract.getContractFileUrl() == null || contract.getContractFileUrl().isEmpty()) {
            throw new IllegalArgumentException("계약서 파일이 존재하지 않습니다.");
        }
    }
    
     */
}