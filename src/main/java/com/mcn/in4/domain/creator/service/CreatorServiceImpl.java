package com.mcn.in4.domain.creator.service;

import com.mcn.in4.domain.creator.dto.request.CreatorRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorResponseDTO;
import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.creator.repository.CreatorRepository;
import com.mcn.in4.domain.member.repository.MemberProfileRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberCreatorDetail;
import com.mcn.in4.domain.member.entity.MemberProfile;
import com.mcn.in4.domain.member.entity.memberEnum.CreatorPlatform;
import com.mcn.in4.domain.member.entity.memberEnum.CreatorStatus;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepository creatorRepository;
    private final CreatorDetailRepository creatorDetailRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final PasswordEncoder passwordEncoder;

    // 크리에이터 생성
    @Override
    @Transactional
    public Long createCreator(CreatorRequestDTO.Create request) {
        validateDuplicateAccount(request.getMemberAccount());

        Member manager = findManager(request.getMemberManagerId());
        Member creator = saveCreator(request);
        saveCreatorDetail(request, creator, manager);
        saveCreatorProfile(creator);

        return creator.getMemberId();
    }

    // 전체 크리에이터 조회
    @Override
    public List<CreatorResponseDTO.Info> getAllCreators(String name) {
        List<Member> creators;

        // 이름 검색어가 있으면 검색, 없으면 전체 조회
        if (name != null && !name.trim().isEmpty()) {
            creators = creatorRepository.findCreatorsByNameWithDepartment(
                    MemberRole.CREATOR, MemberStatus.WORKING, name.trim());
        } else {
            creators = creatorRepository.findAllCreatorsWithDepartment(
                    MemberRole.CREATOR, MemberStatus.WORKING);
        }

        return buildResponseList(creators);
    }

    // 크리에이터 상세 조회
    @Override
    public CreatorResponseDTO.Info getCreatorById(Long creatorId) {
        Member creator = findCreator(creatorId);
        MemberCreatorDetail detail = findCreatorDetail(creatorId);
        MemberProfile profile = findProfile(creatorId);
        return buildResponse(creator, detail, profile);
    }

    // 크리에이터 정보 수정
    @Override
    @Transactional
    public CreatorResponseDTO.Info updateCreator(Long creatorId, CreatorRequestDTO.Update request) {
        Member creator = findCreator(creatorId);
        MemberCreatorDetail detail = findCreatorDetail(creatorId);

        if (hasMemberChange(request)) {
            creator = updateMember(creator, request);
        }

        Member manager = getManager(detail, request.getMemberManagerId());
        detail = updateDetail(creator, detail, manager, request);

        return buildResponse(creator, detail, findProfile(creatorId));
    }

    // 크리에이터 삭제 -> 상태를 변경하여 삭제 처리
    @Override
    @Transactional
    public void deleteCreator(Long creatorId) {
        Member creator = findCreator(creatorId);
        creatorRepository.save(Member.builder()
                .memberId(creator.getMemberId())
                .memberAccount(creator.getMemberAccount())
                .memberPassword(creator.getMemberPassword())
                .memberName(creator.getMemberName())
                .memberRole(creator.getMemberRole())
                .memberStatus(MemberStatus.SUSPENDED)
                .department(creator.getDepartment())
                .build());
    }

    // 매니저별 크리에이터 조회
    @Override
    public List<CreatorResponseDTO.Info> getMyCreators(Long managerId) {
        List<Member> creators = creatorRepository.findCreatorsByManagerIdWithDepartment(
                managerId, MemberRole.CREATOR, MemberStatus.WORKING);
        return buildResponseList(creators);
    }

    // 사번 중복 검사
    private void validateDuplicateAccount(String memberAccount) {
        if (creatorRepository.existsByMemberAccount(memberAccount)) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER_ACCOUNT);
        }
    }

    // 매니저 조회
    private Member findManager(Long managerId) {
        return creatorRepository.findManagerById(managerId, MemberRole.MANAGER)
                .orElseThrow(() -> new CustomException(ErrorCode.MANAGER_NOT_FOUND));
    }

    // 크리에이터 조회
    private Member findCreator(Long creatorId) {
        return creatorRepository.findCreatorByIdWithDepartment(
                creatorId, MemberRole.CREATOR, MemberStatus.WORKING)
                .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND));
    }

    // 크리에이터 상세 정보 조회
    private MemberCreatorDetail findCreatorDetail(Long creatorId) {
        return creatorDetailRepository.findByCreatorIdWithManager(creatorId)
                .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_DETAIL_NOT_FOUND));
    }

    // 프로필 정보 조회
    private MemberProfile findProfile(Long creatorId) {
        return memberProfileRepository.findByMember_MemberId(creatorId).orElse(null);
    }

    // 크리에이터 회원 정보 저장
    private Member saveCreator(CreatorRequestDTO.Create request) {
        return creatorRepository.save(Member.builder()
                .memberAccount(request.getMemberAccount())
                .memberPassword(passwordEncoder.encode(request.getMemberPassword()))
                .memberName(request.getMemberName())
                .memberRole(MemberRole.CREATOR)
                .memberStatus(MemberStatus.WORKING)
                .build());
    }

    // 크리에이터 상세 정보 저장
    private void saveCreatorDetail(CreatorRequestDTO.Create request, Member creator, Member manager) {
        creatorDetailRepository.save(MemberCreatorDetail.builder()
                .memberCreator(creator)
                .memberManager(manager)
                .creatorSubscribe(request.getCreatorSubscribe())
                .creatorCategory(request.getCreatorCategory())
                .creatorPlatform(CreatorPlatform.valueOf(request.getCreatorPlatform()))
                .creatorStatus(CreatorStatus.valueOf(request.getCreatorStatus()))
                .creatorMainContact(request.getCreatorMainContact())
                .build());
    }

    // 회원 정보 변경 여부 확인
    private boolean hasMemberChange(CreatorRequestDTO.Update request) {
        return request.getMemberName() != null ||
                request.getMemberAccount() != null ||
                request.getMemberPassword() != null;
    }

    // 담당 매니저 조회
    private Member getManager(MemberCreatorDetail detail, Long newManagerId) {
        if (newManagerId != null && !newManagerId.equals(detail.getMemberManager().getMemberId())) {
            return findManager(newManagerId);
        }
        return detail.getMemberManager();
    }

    // 크리에이터 회원 정보 업데이트
    private Member updateMember(Member creator, CreatorRequestDTO.Update request) {
        return creatorRepository.save(Member.builder()
                .memberId(creator.getMemberId())
                .memberAccount(
                        request.getMemberAccount() != null ? request.getMemberAccount() : creator.getMemberAccount())
                .memberPassword(
                        request.getMemberPassword() != null ? passwordEncoder.encode(request.getMemberPassword())
                                : creator.getMemberPassword())
                .memberName(request.getMemberName() != null ? request.getMemberName() : creator.getMemberName())
                .memberRole(creator.getMemberRole())
                .memberStatus(creator.getMemberStatus())
                .department(creator.getDepartment())
                .build());
    }

    // 크리에이터 상세 정보 업데이트
    private MemberCreatorDetail updateDetail(Member creator, MemberCreatorDetail detail,
            Member manager, CreatorRequestDTO.Update request) {
        return creatorDetailRepository.save(MemberCreatorDetail.builder()
                .creatorDetailId(detail.getCreatorDetailId())
                .memberCreator(creator)
                .memberManager(manager)
                .creatorSubscribe(request.getCreatorSubscribe() != null ? request.getCreatorSubscribe()
                        : detail.getCreatorSubscribe())
                .creatorCategory(request.getCreatorCategory() != null ? request.getCreatorCategory()
                        : detail.getCreatorCategory())
                .creatorPlatform(
                        request.getCreatorPlatform() != null ? CreatorPlatform.valueOf(request.getCreatorPlatform())
                                : detail.getCreatorPlatform())
                .creatorStatus(request.getCreatorStatus() != null ? CreatorStatus.valueOf(request.getCreatorStatus())
                        : detail.getCreatorStatus())
                .creatorMainContact(request.getCreatorMainContact() != null ? request.getCreatorMainContact()
                        : detail.getCreatorMainContact())
                .build());
    }

    // 크리에이터 리스트를 응답 DTO 리스트로 변환
    private List<CreatorResponseDTO.Info> buildResponseList(List<Member> creators) {
        if (creators.isEmpty()) {
            return List.of();
        }

        List<Long> creatorIds = creators.stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        Map<Long, MemberCreatorDetail> detailMap = creatorDetailRepository
                .findByCreatorIdsWithManager(creatorIds).stream()
                .collect(Collectors.toMap(d -> d.getMemberCreator().getMemberId(), d -> d));

        Map<Long, MemberProfile> profileMap = memberProfileRepository
                .findByMemberIds(creatorIds).stream()
                .collect(Collectors.toMap(p -> p.getMember().getMemberId(), p -> p));

        return creators.stream()
                .map(creator -> {
                    MemberCreatorDetail detail = detailMap.get(creator.getMemberId());
                    if (detail == null) {
                        throw new CustomException(ErrorCode.CREATOR_NOT_FOUND);
                    }
                    return buildResponse(creator, detail, profileMap.get(creator.getMemberId()));
                })
                .collect(Collectors.toList());
    }

    // 단일 크리에이터를 응답 DTO로 변환
    private CreatorResponseDTO.Info buildResponse(Member creator, MemberCreatorDetail detail,
            MemberProfile profile) {
        return profile != null ? CreatorResponseDTO.Info.fromWithProfile(creator, detail,
                profile.getProfileImage(), profile.getProfileBanner()) : CreatorResponseDTO.Info.from(creator, detail);
    }
        // 크리에이터 기본 프로필 저장
    private void saveCreatorProfile(Member creator) {
        memberProfileRepository.save(MemberProfile.builder()
                .member(creator)
                .profileImage("https://i.postimg.cc/qBczxYv8/creator.png")
                .profileBanner("https://i.postimg.cc/d3zpgD4x/photo_1519389950473_47ba0277781c.avif")
                .build());
    }
}
