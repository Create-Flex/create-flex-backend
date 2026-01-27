package com.mcn.in4.domain.creator.service;

import com.mcn.in4.domain.creator.dto.CreatorDto;
import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.creator.repository.CreatorRepository;
import com.mcn.in4.domain.creator.repository.MemberProfileRepository;
import com.mcn.in4.entity.member.Member;
import com.mcn.in4.entity.member.MemberCreatorDetail;
import com.mcn.in4.entity.member.MemberProfile;
import com.mcn.in4.entity.member.memberEnum.CreatorPlatform;
import com.mcn.in4.entity.member.memberEnum.CreatorStatus;
import com.mcn.in4.entity.member.memberEnum.MemberRole;
import com.mcn.in4.entity.member.memberEnum.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorService {

    private final CreatorRepository creatorRepository;
    private final CreatorDetailRepository creatorDetailRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 크리에이터 등록
     */
    @Transactional
    public Long createCreator(String memberName, String creatorPlatform, String creatorSubscribe,
                              String creatorCategory, String memberAccount, String memberPassword,
                              Long memberManagerId, String creatorStatus) {
        // 사번 중복 체크
        if (creatorRepository.existsByMemberAccount(memberAccount)) {
            throw new IllegalArgumentException("이미 존재하는 사번입니다: " + memberAccount);
        }

        // 담당 매니저 조회
        Member manager = creatorRepository.findById(memberManagerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다: " + memberManagerId));

        if (manager.getMemberRole() != MemberRole.MANAGER) {
            throw new IllegalArgumentException("매니저 권한이 없는 사용자입니다");
        }

        // 크리에이터 회원 생성
        Member creator = Member.builder()
                .memberAccount(memberAccount)
                .memberPassword(passwordEncoder.encode(memberPassword))
                .memberName(memberName)
                .memberRole(MemberRole.CREATOR)
                .memberStatus(MemberStatus.WORKING)
                .build();

        creatorRepository.save(creator);

        // Enum 변환
        CreatorPlatform platform = CreatorPlatform.valueOf(creatorPlatform);
        CreatorStatus status = CreatorStatus.valueOf(creatorStatus);

        // 크리에이터 상세 정보 생성
        MemberCreatorDetail creatorDetail = MemberCreatorDetail.builder()
                .memberCreator(creator)
                .memberManager(manager)
                .creatorSubscribe(creatorSubscribe)
                .creatorCategory(creatorCategory)
                .creatorPlatform(platform)
                .creatorStatus(status)
                .build();

        creatorDetailRepository.save(creatorDetail);

        return creator.getMemberId();
    }

    /**
     * 크리에이터 목록 조회 (관리자, 매니저)
     */
    public List<CreatorDto.Response> getAllCreators() {
        List<Member> creators = creatorRepository.findByMemberRoleAndMemberStatus(
                MemberRole.CREATOR, MemberStatus.WORKING);

        return creators.stream()
                .map(creator -> {
                    MemberCreatorDetail detail = creatorDetailRepository
                            .findByMemberCreator_MemberId(creator.getMemberId())
                            .orElseThrow(() -> new IllegalArgumentException("크리에이터 상세 정보가 없습니다"));

                    MemberProfile profile = memberProfileRepository
                            .findByMember_MemberId(creator.getMemberId())
                            .orElse(null);

                    if (profile != null) {
                        return CreatorDto.Response.fromWithProfile(
                                creator, detail, profile.getProfileImage(), profile.getProfileBanner());
                    } else {
                        return CreatorDto.Response.from(creator, detail);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 크리에이터 상세 조회
     */
    public CreatorDto.Response getCreatorById(Long creatorId) {
        Member creator = creatorRepository.findByMemberIdAndMemberRoleAndMemberStatus(
                        creatorId, MemberRole.CREATOR, MemberStatus.WORKING)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다: " + creatorId));

        MemberCreatorDetail detail = creatorDetailRepository
                .findByMemberCreator_MemberId(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("크리에이터 상세 정보가 없습니다"));

        MemberProfile profile = memberProfileRepository
                .findByMember_MemberId(creatorId)
                .orElse(null);

        if (profile != null) {
            return CreatorDto.Response.fromWithProfile(
                    creator, detail, profile.getProfileImage(), profile.getProfileBanner());
        } else {
            return CreatorDto.Response.from(creator, detail);
        }
    }

    /**
     * 크리에이터 정보 수정 (관리자)
     */
    @Transactional
    public CreatorDto.Response updateCreator(Long creatorId, String memberName, String creatorPlatform,
                                             String creatorSubscribe, String creatorCategory, String memberAccount,
                                             String memberPassword, Long memberManagerId, String creatorStatus) {
        Member creator = creatorRepository.findByMemberIdAndMemberRoleAndMemberStatus(
                        creatorId, MemberRole.CREATOR, MemberStatus.WORKING)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다: " + creatorId));

        MemberCreatorDetail detail = creatorDetailRepository
                .findByMemberCreator_MemberId(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("크리에이터 상세 정보가 없습니다"));

        // Member 정보 업데이트
        if (memberName != null || memberAccount != null || memberPassword != null) {
            creator = Member.builder()
                    .memberId(creator.getMemberId())
                    .memberAccount(memberAccount != null ? memberAccount : creator.getMemberAccount())
                    .memberPassword(memberPassword != null ?
                            passwordEncoder.encode(memberPassword) : creator.getMemberPassword())
                    .memberName(memberName != null ? memberName : creator.getMemberName())
                    .memberRole(creator.getMemberRole())
                    .memberStatus(creator.getMemberStatus())
                    .department(creator.getDepartment())
                    .build();
            creatorRepository.save(creator);
        }

        // CreatorDetail 정보 업데이트
        Member manager = detail.getMemberManager();
        if (memberManagerId != null) {
            manager = creatorRepository.findById(memberManagerId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다"));

            if (manager.getMemberRole() != MemberRole.MANAGER) {
                throw new IllegalArgumentException("매니저 권한이 없는 사용자입니다");
            }
        }

        // Enum 변환
        CreatorPlatform platform = creatorPlatform != null ?
                CreatorPlatform.valueOf(creatorPlatform) : detail.getCreatorPlatform();
        CreatorStatus status = creatorStatus != null ?
                CreatorStatus.valueOf(creatorStatus) : detail.getCreatorStatus();

        detail = MemberCreatorDetail.builder()
                .creatorDetailId(detail.getCreatorDetailId())
                .memberCreator(creator)
                .memberManager(manager)
                .creatorSubscribe(creatorSubscribe != null ? creatorSubscribe : detail.getCreatorSubscribe())
                .creatorCategory(creatorCategory != null ? creatorCategory : detail.getCreatorCategory())
                .creatorPlatform(platform)
                .creatorStatus(status)
                .build();
        creatorDetailRepository.save(detail);

        MemberProfile profile = memberProfileRepository
                .findByMember_MemberId(creatorId)
                .orElse(null);

        if (profile != null) {
            return CreatorDto.Response.fromWithProfile(
                    creator, detail, profile.getProfileImage(), profile.getProfileBanner());
        } else {
            return CreatorDto.Response.from(creator, detail);
        }
    }

    /**
     * 크리에이터 삭제 (관리자) - 소프트 삭제
     */
    @Transactional
    public void deleteCreator(Long creatorId) {
        Member creator = creatorRepository.findByMemberIdAndMemberRoleAndMemberStatus(
                        creatorId, MemberRole.CREATOR, MemberStatus.WORKING)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크리에이터입니다: " + creatorId));

        // 상태를 SUSPENDED로 변경 (소프트 삭제)
        Member updatedCreator = Member.builder()
                .memberId(creator.getMemberId())
                .memberAccount(creator.getMemberAccount())
                .memberPassword(creator.getMemberPassword())
                .memberName(creator.getMemberName())
                .memberRole(creator.getMemberRole())
                .memberStatus(MemberStatus.SUSPENDED)
                .department(creator.getDepartment())
                .build();

        creatorRepository.save(updatedCreator);
    }

    /**
     * 내 담당 크리에이터 조회 (매니저)
     */
    public List<CreatorDto.Response> getMyCreators(Long managerId) {
        List<Member> creators = creatorRepository.findCreatorsByManagerId(
                managerId, MemberRole.CREATOR, MemberStatus.WORKING);

        return creators.stream()
                .map(creator -> {
                    MemberCreatorDetail detail = creatorDetailRepository
                            .findByMemberCreator_MemberId(creator.getMemberId())
                            .orElseThrow(() -> new IllegalArgumentException("크리에이터 상세 정보가 없습니다"));

                    MemberProfile profile = memberProfileRepository
                            .findByMember_MemberId(creator.getMemberId())
                            .orElse(null);

                    if (profile != null) {
                        return CreatorDto.Response.fromWithProfile(
                                creator, detail, profile.getProfileImage(), profile.getProfileBanner());
                    } else {
                        return CreatorDto.Response.from(creator, detail);
                    }
                })
                .collect(Collectors.toList());
    }


}