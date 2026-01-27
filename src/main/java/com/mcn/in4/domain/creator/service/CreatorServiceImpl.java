package com.mcn.in4.domain.creator.service;

import com.mcn.in4.domain.creator.dto.request.CreatorRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorResponseDTO;
import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.creator.repository.CreatorRepository;
import com.mcn.in4.domain.creator.repository.MemberProfileRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberCreatorDetail;
import com.mcn.in4.domain.member.entity.MemberProfile;
import com.mcn.in4.domain.member.entity.memberEnum.CreatorPlatform;
import com.mcn.in4.domain.member.entity.memberEnum.CreatorStatus;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
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

    @Override
    @Transactional
    public Long createCreator(CreatorRequestDTO.Create request) {
        validateDuplicateAccount(request.getMemberAccount());

        Member manager = findManager(request.getMemberManagerId());
        Member creator = saveCreator(request);
        saveCreatorDetail(request, creator, manager);

        return creator.getMemberId();
    }

    @Override
    public List<CreatorResponseDTO.Info> getAllCreators() {
        List<Member> creators = creatorRepository.findAllCreatorsWithDepartment(
                MemberRole.CREATOR, MemberStatus.WORKING);
        return buildResponseList(creators);
    }

    @Override
    public CreatorResponseDTO.Info getCreatorById(Long creatorId) {
        Member creator = findCreator(creatorId);
        MemberCreatorDetail detail = findCreatorDetail(creatorId);
        MemberProfile profile = findProfile(creatorId);
        return buildResponse(creator, detail, profile);
    }

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

    @Override
    public List<CreatorResponseDTO.Info> getMyCreators(Long managerId) {
        List<Member> creators = creatorRepository.findCreatorsByManagerIdWithDepartment(
                managerId, MemberRole.CREATOR, MemberStatus.WORKING);
        return buildResponseList(creators);
    }

    // ========== Private Methods ==========

    private void validateDuplicateAccount(String memberAccount) {
        if (creatorRepository.existsByMemberAccount(memberAccount)) {
            throw new IllegalArgumentException("이미 존재하는 사번입니다: " + memberAccount);
        }
    }

    private Member findManager(Long managerId) {
        return creatorRepository.findManagerById(managerId, MemberRole.MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다: " + managerId));
    }

    private Member findCreator(Long creatorId) {
        return creatorRepository.findCreatorByIdWithDepartment(
                        creatorId, MemberRole.CREATOR, MemberStatus.WORKING)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다: " + creatorId));
    }

    private MemberCreatorDetail findCreatorDetail(Long creatorId) {
        return creatorDetailRepository.findByCreatorIdWithManager(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("크리에이터 상세 정보가 없습니다"));
    }

    private MemberProfile findProfile(Long creatorId) {
        return memberProfileRepository.findByMember_MemberId(creatorId).orElse(null);
    }

    private Member saveCreator(CreatorRequestDTO.Create request) {
        return creatorRepository.save(Member.builder()
                .memberAccount(request.getMemberAccount())
                .memberPassword(passwordEncoder.encode(request.getMemberPassword()))
                .memberName(request.getMemberName())
                .memberRole(MemberRole.CREATOR)
                .memberStatus(MemberStatus.WORKING)
                .build());
    }

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

    private boolean hasMemberChange(CreatorRequestDTO.Update request) {
        return request.getMemberName() != null ||
                request.getMemberAccount() != null ||
                request.getMemberPassword() != null;
    }

    private Member getManager(MemberCreatorDetail detail, Long newManagerId) {
        if (newManagerId != null && !newManagerId.equals(detail.getMemberManager().getMemberId())) {
            return findManager(newManagerId);
        }
        return detail.getMemberManager();
    }

    private Member updateMember(Member creator, CreatorRequestDTO.Update request) {
        return creatorRepository.save(Member.builder()
                .memberId(creator.getMemberId())
                .memberAccount(request.getMemberAccount() != null ?
                        request.getMemberAccount() : creator.getMemberAccount())
                .memberPassword(request.getMemberPassword() != null ?
                        passwordEncoder.encode(request.getMemberPassword()) : creator.getMemberPassword())
                .memberName(request.getMemberName() != null ?
                        request.getMemberName() : creator.getMemberName())
                .memberRole(creator.getMemberRole())
                .memberStatus(creator.getMemberStatus())
                .department(creator.getDepartment())
                .build());
    }

    private MemberCreatorDetail updateDetail(Member creator, MemberCreatorDetail detail,
                                             Member manager, CreatorRequestDTO.Update request) {
        return creatorDetailRepository.save(MemberCreatorDetail.builder()
                .creatorDetailId(detail.getCreatorDetailId())
                .memberCreator(creator)
                .memberManager(manager)
                .creatorSubscribe(request.getCreatorSubscribe() != null ?
                        request.getCreatorSubscribe() : detail.getCreatorSubscribe())
                .creatorCategory(request.getCreatorCategory() != null ?
                        request.getCreatorCategory() : detail.getCreatorCategory())
                .creatorPlatform(request.getCreatorPlatform() != null ?
                        CreatorPlatform.valueOf(request.getCreatorPlatform()) : detail.getCreatorPlatform())
                .creatorStatus(request.getCreatorStatus() != null ?
                        CreatorStatus.valueOf(request.getCreatorStatus()) : detail.getCreatorStatus())
                .creatorMainContact(request.getCreatorMainContact() != null ?
                        request.getCreatorMainContact() : detail.getCreatorMainContact())
                .build());
    }

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
                        throw new IllegalArgumentException("크리에이터 상세 정보가 없습니다: " + creator.getMemberId());
                    }
                    return buildResponse(creator, detail, profileMap.get(creator.getMemberId()));
                })
                .collect(Collectors.toList());
    }

    private CreatorResponseDTO.Info buildResponse(Member creator, MemberCreatorDetail detail,
                                                  MemberProfile profile) {
        return profile != null ?
                CreatorResponseDTO.Info.fromWithProfile(creator, detail,
                        profile.getProfileImage(), profile.getProfileBanner()) :
                CreatorResponseDTO.Info.from(creator, detail);
    }
}