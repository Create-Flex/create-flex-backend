package com.mcn.in4.domain.member.service;

import com.mcn.in4.domain.member.dto.MemberProfileResponseDto;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.entity.MemberProfile;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberProfileRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;

    @Override
    public MemberProfileResponseDto getMemberProfile(Long memberId) {
        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. id=" + memberId));

        // 2. 프로필 조회
        MemberProfile profile = memberProfileRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("프로필 정보를 찾을 수 없습니다. memberId=" + memberId));

        // 3. 빌더 초기화 (공통 정보)
        MemberProfileResponseDto.MemberProfileResponseDtoBuilder builder = MemberProfileResponseDto.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .memberAccount(member.getMemberAccount())
                .memberRole(member.getMemberRole())
                .profileImage(profile.getProfileImage())
                .profileBanner(profile.getProfileBanner());

        // 4. 직원인 경우 상세 정보 조회 및 추가
        if (member.getMemberRole() == MemberRole.EMPLOYEE || member.getMemberRole() == MemberRole.MANAGER
                || member.getMemberRole() == MemberRole.ADMINISTRATOR) {
            MemberEmployeeDetail employeeDetail = memberEmployeeDetailRepository.findByMember(member)
                    .orElse(null);

            if (employeeDetail != null) {
                builder.task(employeeDetail.getTask())
                        .nickname(employeeDetail.getNickname())
                        .departmentName(employeeDetail.getDepartment().getDepartmentName()) // 부서명
                        .engName(employeeDetail.getEngName())
                        .personalEmail(employeeDetail.getPersonalEmail())
                        .personalCall(employeeDetail.getPersonalCall())
                        .hireDate(employeeDetail.getHireDate())
                        .address(employeeDetail.getAddress())
                        .vacationRemainder(employeeDetail.getVacationRemainder());
            }
        }

        return builder.build();
    }
}
