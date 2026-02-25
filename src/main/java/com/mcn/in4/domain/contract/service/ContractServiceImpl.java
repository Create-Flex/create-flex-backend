package com.mcn.in4.domain.contract.service;

import com.mcn.in4.domain.contract.dto.request.ContractRequestDTO;
import com.mcn.in4.domain.contract.dto.response.ContractResponseDTO;
import com.mcn.in4.domain.contract.entity.CreatorContract;
import com.mcn.in4.domain.contract.repository.ContractRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractServiceImpl implements ContractService {

        private final ContractRepository contractRepository;
        private final S3Presigner s3Presigner;

        @Value("${aws.region}")
        private String region;

        @Value("${aws.s3.bucket}")
        private String bucket;

        @Override
        @Transactional
        public ContractResponseDTO.Create createContract(ContractRequestDTO.Create request) {
                // 계약 기간 검증
                if (request.getContractStart().isAfter(request.getContractEnd())) {
                        throw new CustomException(ErrorCode.INVALID_CONTRACT_PERIOD);
                }

                MultipartFile file = request.getFile();
                String contractFileUrl = request.getContractFileUrl();
                String presignedUrl = null;

                if (file != null && !file.isEmpty()) {
                        String fileName = file.getOriginalFilename();
                        String s3key = "public/contract/" + UUID.randomUUID().toString() + "/" + fileName;

                        // S3 업로드를 위한 Presigned URL 생성
                        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                        .bucket(bucket)
                                        .key(s3key)
                                        .contentType(file.getContentType())
                                        .build();

                        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                                        PutObjectPresignRequest.builder()
                                                        .putObjectRequest(putObjectRequest)
                                                        .signatureDuration(Duration.ofHours(1))
                                                        .build());

                        presignedUrl = presignedRequest.url().toString();

                        // S3에서 파일을 조회할 수 있는 URL
                        contractFileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + s3key;
                }

                // 계약 엔티티 생성 및 저장
                CreatorContract contract = CreatorContract.builder()
                                .contractName(request.getContractName())
                                .creatorName(request.getCreatorName())
                                .contractStart(request.getContractStart())
                                .contractEnd(request.getContractEnd())
                                .contractFileUrl(contractFileUrl)
                                .build();

                CreatorContract savedContract = contractRepository.save(contract);

                return ContractResponseDTO.Create.builder()
                                .contractId(savedContract.getCreatorContractId())
                                .presignedUrl(presignedUrl)
                                .build();
        }

        @Override
        public List<ContractResponseDTO.Info> getAllContracts(String name) {
                List<CreatorContract> contracts = contractRepository.findAllContractsWithCreator(name);

                return contracts.stream()
                                .map(ContractResponseDTO.Info::from)
                                .collect(Collectors.toList());
        }
}