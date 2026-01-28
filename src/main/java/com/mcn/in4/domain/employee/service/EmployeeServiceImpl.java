package com.mcn.in4.domain.employee.service;

import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final MemberRepository memberRepository;
    private final MemberEmployeeDetailRepository detailRepository;
    @Override
    public EmployeeResponseDTO.EmployeeDetailResponseDto getEmployeeDetail(Long id) {
        // Member 체크
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다 : " + id));
        //  MemberEmployeeDetail 정보 조회 (직원 상세 테이블)
        MemberEmployeeDetail detail = detailRepository.findByMemberMemberId(id)
                .orElseThrow(() -> new IllegalArgumentException("직원 상세 정보가 존재하지 않습니다. ID: " + id));
        // DTO 변환
        return EmployeeResponseDTO.EmployeeDetailResponseDto.builder()
                .memberAccount(member.getMemberAccount())
                .memberName(member.getMemberName())
                .memberRole(member.getMemberRole())
                .memberStatus(member.getMemberStatus())
                .task(member.getTask())
                .departmentName(member.getDepartment() != null ? member.getDepartment().getDepartmentName() : null)
                .nickname(detail.getNickname())
                .personalEmail(detail.getPersonalEmail())
                .personalCall(detail.getPersonalCall())
                .address(detail.getAddress())
                .engName(detail.getEngName())
                .build();
    }
}
