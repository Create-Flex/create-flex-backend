package com.mcn.in4.domain.member.service;

import com.mcn.in4.domain.member.dto.ManagerResponseDto;
import com.mcn.in4.domain.member.dto.MemberProfileResponseDto;
import com.mcn.in4.domain.member.dto.MemberSummaryDto;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.entity.MemberProfile;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberProfileRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class MemberServiceImpl implements MemberService {

        private final MemberRepository memberRepository;
        private final MemberProfileRepository memberProfileRepository;
        private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;
        private final S3Presigner s3Presigner;

        @Value("${aws.region}")
        private String region;

        @Value("${aws.s3.bucket}")
        private String bucket;

        @Override
        public MemberProfileResponseDto getMemberProfile(Long memberId) {
                // 1. 회원 + 부서 한 번에 조회 (FETCH JOIN으로 department lazy loading 방지)
                Member member = memberRepository.findByIdWithDepartment(memberId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. id=" + memberId));

                // 2. 프로필 조회 (memberId로 직접 조회 - 불필요한 member 재조회 방지)
                MemberProfile profile = memberProfileRepository.findByMember_MemberId(memberId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "프로필 정보를 찾을 수 없습니다. memberId=" + memberId));

                // 3. 빌더 초기화 (공통 정보)
                // 프로필 이미지: 이미 전체 URL이면 그대로 사용, S3 키면 S3 URL 생성
                String profileImageUrl = profile.getProfileImage();
                if (profileImageUrl != null && !profileImageUrl.startsWith("http")) {
                        profileImageUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + profileImageUrl;
                }

                MemberProfileResponseDto.MemberProfileResponseDtoBuilder builder = MemberProfileResponseDto.builder()
                                .memberId(member.getMemberId())
                                .memberName(member.getMemberName())
                                .memberAccount(member.getMemberAccount())
                                .memberRole(member.getMemberRole())
                                .profileImage(profileImageUrl)
                                .profileBanner(profile.getProfileBanner());

                // 4. 직원인 경우 상세 정보 조회 및 추가 (memberId로 직접 조회)
                if (member.getMemberRole() == MemberRole.EMPLOYEE || member.getMemberRole() == MemberRole.MANAGER
                                || member.getMemberRole() == MemberRole.ADMINISTRATOR) {
                        MemberEmployeeDetail employeeDetail = memberEmployeeDetailRepository
                                        .findByMemberMemberId(memberId)
                                        .orElse(null);

                        if (employeeDetail != null) {
                                builder.task(member.getTask())
                                                .nickname(employeeDetail.getNickname())
                                                .departmentName(member.getDepartment() != null
                                                                ? member.getDepartment().getDepartmentName()
                                                                : null)
                                                .engName(employeeDetail.getEngName())
                                                .personalEmail(employeeDetail.getPersonalEmail())
                                                .corporEmail(employeeDetail.getCorporEmail())
                                                .personalCall(employeeDetail.getPersonalCall())
                                                .hireDate(employeeDetail.getHireDate())
                                                .address(employeeDetail.getAddress())
                                                .vacationRemainder(employeeDetail.getVacationRemainder());
                        }
                }

                return builder.build();
        }

        // 멤버 +부서 간단 정보 가저오는
        @Override
        public List<MemberSummaryDto> getAllMembers() {

                List<Member> members = memberRepository.findAllWithDepartment();

                List<MemberProfile> profiles = memberProfileRepository.findAll();

                Map<Long, String> profileMap = profiles.stream()
                                .collect(Collectors.toMap(
                                                p -> p.getMember().getMemberId(),
                                                p -> {
                                                        String img = p.getProfileImage();
                                                        if (img != null && !img.startsWith("http")) {
                                                                return "https://" + bucket + ".s3." + region
                                                                                + ".amazonaws.com/" + img;
                                                        }
                                                        return img;
                                                }));

                return members.stream()
                                .map(member -> MemberSummaryDto
                                                .from(
                                                                member,
                                                                profileMap.getOrDefault(member.getMemberId(), null) // 프로필
                                                                                                                    // 없으면
                                                                                                                    // null
                                                ))
                                .collect(Collectors.toList());
        }

        public MemberProfileResponseDto generatePresignedUrl(Long memberId, MultipartFile file) {
                String fileName = file.getOriginalFilename();
                String s3key = "public/profile/" + UUID.randomUUID().toString() + "/" + fileName;

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(s3key)
                                .contentType(file.getContentType())
                                .build();

                PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                                PutObjectPresignRequest.builder()
                                                .putObjectRequest(putObjectRequest)
                                                .signatureDuration(Duration.ofHours(1)) // 유효시간 1시간
                                                .build());

                String presignedUrl = presignedRequest.url().toString();

                String viewUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + s3key;

                memberProfileRepository.updateProfileImage(memberId, s3key);

                return MemberProfileResponseDto.builder()
                                .presignedURL(presignedUrl)
                                .build();
        }

        @Override
        public List<ManagerResponseDto.ManagerInfo> getAllManagers() {
                List<Member> managers = memberRepository.findAllManagersWithDepartment(
                                MemberRole.MANAGER,
                                MemberStatus.WORKING);

                return managers.stream()
                                .map(ManagerResponseDto.ManagerInfo::from)
                                .collect(Collectors.toList());
        }

        @Override
        public MemberProfileResponseDto deleteMemberProfile(Long memberId) {
                MemberProfile deletedProfile = memberProfileRepository.findTopByMember_MemberId(memberId).orElseThrow();

                String s3key = deletedProfile.getProfileImage();

                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                .bucket(bucket)
                                .key(s3key)
                                .build();

                PresignedDeleteObjectRequest presignedDeleteObjectRequest = s3Presigner.presignDeleteObject(
                                DeleteObjectPresignRequest.builder()
                                                .deleteObjectRequest(deleteObjectRequest)
                                                .signatureDuration(Duration.ofMinutes(5))
                                                .build());

                String presignedUrl = presignedDeleteObjectRequest.url().toString();

                return MemberProfileResponseDto.builder()
                                .presignedURL(presignedUrl)
                                .build();
        }
}
