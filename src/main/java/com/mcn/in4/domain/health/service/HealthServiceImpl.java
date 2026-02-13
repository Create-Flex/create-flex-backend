package com.mcn.in4.domain.health.service;

import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.health.dto.HealthResponseDto.*;
import com.mcn.in4.domain.health.dto.HealthSummanaryCountDto;
import com.mcn.in4.domain.health.dto.MentalHealthDto;
import com.mcn.in4.domain.health.entity.CheckupSummanary;
import com.mcn.in4.domain.health.entity.CreatorMentalHealth;
import com.mcn.in4.domain.health.entity.Health;
import com.mcn.in4.domain.health.repository.CreatorMentalHealthRepository;
import com.mcn.in4.domain.health.repository.HealthRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.DeleteObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedDeleteObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class HealthServiceImpl implements HealthService{

    private final HealthRepository healthRepository;
    private final MemberRepository memberRepository;
    private final CreatorDetailRepository creatorDetailRepository;
    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;
    private final CreatorMentalHealthRepository creatorMentalHealthRepository;
    private final S3Presigner s3Presigner;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public MypageHealthInfo generateMypageHealthInfo(Long memberId, LocalDate startDate, LocalDate endDate) {
        List<HealthInfo> memberHealthInfo = healthRepository.findByMember_MemberIdAndCheckupDateBetween(
                memberId,
                startDate,
                endDate).stream()
                .map(HealthInfo::from)
                .toList();

        int year = LocalDate.now().getYear();
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end   = LocalDate.of(year, 12, 31);
        boolean memberHaveHealthCheck = healthRepository.existsByMember_MemberIdAndCheckupDateBetween(memberId, start, end);
        String memberName = memberRepository.findByMemberId(memberId).getMemberName();

        return new MypageHealthInfo(memberName, memberHealthInfo, memberHaveHealthCheck);
    }

    public CreatorHealthInfo generateCreatorHealthInfo(Long memberId) {
        CreatorHealthInfo creatorHealthInfo = new CreatorHealthInfo();
        MemberRole role = memberRepository.findMemberRoleByMemberId(memberId);
        if (role==MemberRole.ADMINISTRATOR){
            creatorHealthInfo = generateCreatorForAdmin();
        } else if(role==MemberRole.CREATOR) {
            creatorHealthInfo = generateCreatorForCreator(memberId);
        }else{
            creatorHealthInfo = generateCreatorForManager(memberId);
        }

        return creatorHealthInfo;
    }

    public CreatorHealthInfo generateCreatorForManager(Long memberId){
        List<Long> creatorIds = creatorDetailRepository.findCreatorIdsByManagerId(memberId);
        List<Member> creators = memberRepository.findByMemberIdIn(creatorIds);
        List<HealthSummanaryCountDto> creatorHealthInfoA = healthRepository.countGroupedByCheckupSummanaryForMembers(creatorIds);
        List<HealthInfo> creatorHealthInfoB = creators.stream().flatMap(member ->
                healthRepository.findTopByMember_MemberIdOrderByCheckupDateDesc(member.getMemberId())
                        .stream()
                        .map(health -> HealthInfo.from(health, member.getMemberName()))).toList();
        List<MentalHealthDto> creatorHealthInfoC = creatorMentalHealthRepository.findLatestMentalHealthByMemberIds(creatorIds);
        List<MentalHealthDto> creatorHealthInfoD = creatorMentalHealthRepository.findLatestMentalHealthByMemberIdsWhoesDanger(creatorIds);
        return new CreatorHealthInfo(creatorHealthInfoA, creatorHealthInfoB, creatorHealthInfoC, creatorHealthInfoD);
    }

    public CreatorHealthInfo generateCreatorForAdmin(){
        List<Long> creatorIds = creatorDetailRepository.findCreatorIds();
        List<Member> creators = memberRepository.findByMemberIdIn(creatorIds);
        List<HealthSummanaryCountDto> creatorHealthInfoA = healthRepository.countGroupedByCheckupSummanaryForMembers();
        List<HealthInfo> creatorHealthInfoB = creators.stream().flatMap(member ->
                healthRepository.findTopByMember_MemberIdOrderByCheckupDateDesc(member.getMemberId())
                        .stream()
                        .map(health -> HealthInfo.from(health, member.getMemberName()))).toList();
        List<MentalHealthDto> creatorHealthInfoC = creatorMentalHealthRepository.findLatestMentalHealthByMemberIds(creatorIds);
        List<MentalHealthDto> creatorHealthInfoD = creatorMentalHealthRepository.findLatestMentalHealthByMemberIdsWhoesDanger(creatorIds);
        return new CreatorHealthInfo(creatorHealthInfoA, creatorHealthInfoB, creatorHealthInfoC, creatorHealthInfoD);
    }

    public CreatorHealthInfo generateCreatorForCreator(Long memberId){
        List<Long> creatorId = Collections.singletonList(memberId);
        List<Member> creators = memberRepository.findByMemberIdIn(creatorId);
        List<HealthSummanaryCountDto> creatorHealthInfoA = healthRepository.countGroupedByCheckupSummanaryForMembers();
        List<HealthInfo> creatorHealthInfoB = creators.stream().flatMap(member ->
                healthRepository.findTopByMember_MemberIdOrderByCheckupDateDesc(member.getMemberId())
                        .stream()
                        .map(health -> HealthInfo.from(health, member.getMemberName()))).toList();
        List<MentalHealthDto> creatorHealthInfoC = creatorMentalHealthRepository.findLatestMentalHealthByMemberIds(creatorId);
        List<MentalHealthDto> creatorHealthInfoD = creatorMentalHealthRepository.findLatestMentalHealthByMemberIdsWhoesDanger(creatorId);
        return new CreatorHealthInfo(creatorHealthInfoA, creatorHealthInfoB, creatorHealthInfoC, creatorHealthInfoD);
    }

    public AssembledHealthInfo generateManageHealthInfo() {
        List<Long> employeeIds = memberEmployeeDetailRepository.findEmployeeIds();
        List<HealthSummanaryCountDto> employeeCount = healthRepository.countGroupedByCheckupSummanaryForMembers(employeeIds);
        List<Member> employees = memberRepository.findByMemberIdIn(employeeIds);
        List<HealthInfo> employeeInfo = employees.stream().flatMap(member ->
                        healthRepository.findTopByMember_MemberIdOrderByCheckupDateDesc(member.getMemberId())
                                .stream()
                                .map(health -> HealthInfo.from(health, member.getMemberName()))).toList();
        return new AssembledHealthInfo(employeeInfo, employeeCount);
    }

    public AssembledHealthInfo findByNameAndPeriod(String name, LocalDate startDate, LocalDate endDate) {
        List<Long> employeeIds = memberEmployeeDetailRepository.findEmployeeIds();
        List<HealthSummanaryCountDto> employeeCount = healthRepository.countGroupedByCheckupSummanaryForMembers(employeeIds);
        List<Member> employees = memberRepository.findByMemberNameContainingAndMemberIdIn(name, employeeIds);
        List<HealthInfo> employeeInfo = employees.stream().flatMap(member ->
                    healthRepository.findByMember_MemberIdAndCheckupDateBetween(member.getMemberId(), startDate, endDate)
                            .stream()
                            .map(health -> HealthInfo.from(health, member.getMemberName()))).toList();
        return new AssembledHealthInfo(employeeInfo, employeeCount);
    }

    public AssembledHealthInfo findByName(String name){
        List<Long> employeeIds = memberEmployeeDetailRepository.findEmployeeIds();
        List<HealthSummanaryCountDto> employeeCount = healthRepository.countGroupedByCheckupSummanaryForMembers(employeeIds);
        List<Member> employees = memberRepository.findByMemberNameContainingAndMemberIdIn(name, employeeIds);
        List<HealthInfo> employeeInfo = employees.stream().flatMap(member ->
                healthRepository.findByMember_MemberId(member.getMemberId()).stream()
                        .map(health -> HealthInfo.from(health, member.getMemberName()))).toList();
        return new AssembledHealthInfo(employeeInfo, employeeCount);
    }

    public AssembledHealthInfo findByPeriod(LocalDate startDate, LocalDate endDate) {
        List<Long> employeeIds = memberEmployeeDetailRepository.findEmployeeIds();
        List<HealthSummanaryCountDto> employeeCount = healthRepository.countGroupedByCheckupSummanaryForMembers(employeeIds);
        List<Member> employees = memberRepository.findByMemberIdIn(employeeIds);
        List<HealthInfo> employeeInfo = employees.stream().flatMap(member ->
                healthRepository.findByMember_MemberIdAndCheckupDateBetween(member.getMemberId(), startDate, endDate)
                        .stream()
                        .map(health -> HealthInfo.from(health, member.getMemberName()))).toList();
        return new AssembledHealthInfo(employeeInfo, employeeCount);
    }

    public AssembledHealthInfo findAll(){
        List<Long> employeeIds = memberEmployeeDetailRepository.findEmployeeIds();
        List<HealthSummanaryCountDto> employeeCount = healthRepository.countGroupedByCheckupSummanaryForMembers(employeeIds);
        List<Member> employees = memberRepository.findByMemberIdIn(employeeIds);
        List<HealthInfo> employeeInfo = employees.stream().flatMap(member ->
                healthRepository.findByMember_MemberId(member.getMemberId())
                        .stream()
                        .map(health -> HealthInfo.from(health, member.getMemberName()))).toList();
        return new AssembledHealthInfo(employeeInfo, employeeCount);
    }


    public HealthPresigned generatePresignedUrl(Long memberId, String checkupName, LocalDate date, CheckupSummanary checkupSummanary, MultipartFile file){
        String fileName = file.getOriginalFilename();
        String s3key ="public/health/" + UUID.randomUUID().toString() + "/" + fileName;
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

        Health health =Health.builder()
                .member(member)
                .checkupName(checkupName)
                .checkupDate(date)
                .checkupSummanary(checkupSummanary)
                .checkupFileUrl("https://" + bucket + ".s3." + region + ".amazonaws.com/" + s3key)
                .build();

        Health savedHealth = healthRepository.save(health);

        return HealthPresigned.builder()
                .presignedUrl(presignedUrl)
                .build();
    }

    public void saveMentalHealthTest(Long memberId, Long score){
        LocalDate today = LocalDate.now();
        Member creator = Member.builder()
                .memberId(memberId)
                .build();
        CreatorMentalHealth mentalHealth = CreatorMentalHealth.builder()
                .creatorMentalDate(today)
                .creatorMentalScore(score)
                .member(creator)
                .build();
        creatorMentalHealthRepository.save(mentalHealth);
    }

    @Transactional
    public HealthPresigned deleteByHealthId(Long healthID){
        Health deletedHealth = healthRepository.findTopByHealthId(healthID).orElseThrow();

        String s3key = deletedHealth.getCheckupFileUrl();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(s3key)
                .build();

        PresignedDeleteObjectRequest presignedDeleteObjectRequest = s3Presigner.presignDeleteObject(
                DeleteObjectPresignRequest.builder()
                        .deleteObjectRequest(deleteObjectRequest)
                        .signatureDuration(Duration.ofMinutes(5))
                        .build()
        );

        healthRepository.delete(deletedHealth);

        String presignedUrl = presignedDeleteObjectRequest.url().toString();

        return HealthPresigned.builder()
                .presignedUrl(presignedUrl)
                .build();
    }
}
