package com.mcn.in4.domain.health.service;

import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthPresigned;
import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthInfo;
import com.mcn.in4.domain.health.entity.CheckupSummanary;
import com.mcn.in4.domain.health.entity.Health;
import com.mcn.in4.domain.health.repository.HealthRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthServiceImpl implements HealthService{

    private final HealthRepository healthRepository;
    private final MemberRepository memberRepository;
    private final CreatorDetailRepository creatorDetailRepository;
    private final S3Presigner s3Presigner;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public List<HealthInfo> generateHealthInfo(Long memberId, LocalDate startDate, LocalDate endDate) {
        return healthRepository.findByMember_MemberIdAndCheckupDateBetween(
                memberId,
                startDate,
                endDate).stream()
                .map(HealthInfo::from)
                .toList();
    }

    public List<HealthInfo> generateCreatorHealthInfo(Long memberId, LocalDate startDate, LocalDate endDate) {
        List<Long> creatorIds = creatorDetailRepository.findCreatorIdsByManagerId(memberId);
        List<Member> creators = memberRepository.findByMemberIdIn(creatorIds);
        return creators.stream()
                .flatMap(member ->
                        healthRepository
                                .findTopByMember_MemberIdAndCheckupDateBetween(
                                        member.getMemberId(), startDate, endDate
                                )
                                .stream() // Optional → 0 or 1 Stream
                                .map(health -> HealthInfo.from(health, member.getMemberName()))
                )
                .toList();
    }

    public HealthPresigned generatePresignedUrl(Long memberId, String checkupName, LocalDate date, CheckupSummanary checkupSummanary, MultipartFile file){
        String fileName = file.getOriginalFilename();
        String s3key ="public/" + UUID.randomUUID().toString() + "/" + fileName;
        Member member = Member.builder()
                .memberId(memberId)
                .build();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3key)
                .contentType(file.getContentType())
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
            PutObjectPresignRequest.builder()
                    .putObjectRequest(putObjectRequest)
                    .signatureDuration(Duration.ofHours(1))
                    .build()
        );

        String presignedUrl = presignedRequest.url().toString();

        String viewUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + s3key;

        Health health =Health.builder()
                .member(member)
                .checkupName(checkupName)
                .checkupDate(date)
                .checkupSummanary(checkupSummanary)
                .checkupFileUrl(viewUrl)
                .build();

        Health savedHealth = healthRepository.save(health);

        return HealthPresigned.builder()
                .presignedUrl(presignedUrl)
                .build();
    }
}
