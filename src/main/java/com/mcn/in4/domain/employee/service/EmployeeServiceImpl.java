package com.mcn.in4.domain.employee.service;

import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.repository.AttendanceRepository;
import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final MemberRepository memberRepository;
    private final MemberEmployeeDetailRepository detailRepository;
    private final AttendanceRepository attendanceRepository;
    private final VacationRepository vacationRepository; // 휴가 조회를 위해 필요


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

    @Override
    public EmployeeResponseDTO.EmployeeManagementResponseDto getEmployeeManagementList() {
        LocalDate today = LocalDate.now();

        // 전체 데이터 조회
        List<Member> members = memberRepository.findAll();
        List<MemberEmployeeDetail> details = detailRepository.findAll();

        // 직원 리스트 가져오기  근태 상태 포함 해서
        List<EmployeeResponseDTO.EmployeeListDto> list = members.stream().map(member -> {
            MemberEmployeeDetail detail = details.stream()
                    .filter(d -> d.getMember().getMemberId().equals(member.getMemberId()))
                    .findFirst().orElse(null);
            // 해당 직원의 오늘 근태 상태
            String status = attendanceRepository.findByMemberAndAttendanceDate(member, today)
                    .map(Attendance::getAttendanceStatus)
                    .orElse("미출근"); // 기록 없으면 미출근 표시
            return EmployeeResponseDTO.EmployeeListDto.builder()
                    .memberName(member.getMemberName())
                    .departmentName(member.getDepartment() != null ? member.getDepartment().getDepartmentName() : "소속 없음")
                    .task(member.getTask())
                    .memberAccount(member.getMemberAccount())
                    .corporEmail(detail != null ? detail.getCorporEmail() : null)
                    .personalCall(detail != null ? detail.getPersonalCall() : null)
                    .hireDate(detail != null ? detail.getHireDate() : null)
                    .attendanceStatus(status)
                    .build();
        }).collect(Collectors.toList());
        //  요약 통계 직원수
        long totalCount = members.size();

        // 근무 중:  상태가 '근무중' , '출근'인 사람 필터링
        long workingCount = list.stream()
                .filter(e -> "근무중".equals(e.getAttendanceStatus()) || "출근".equals(e.getAttendanceStatus()))
                .count();
        // 휴가/부재 통계 : 휴가자 수 통계
        long vacationCount = vacationRepository.countActiveVacations(today, VacationApprove.APPROVED);
        // 신규 입사자  hireDate 기준 최근 1년 이내
        long newHireCount = details.stream()
                .filter(d -> d.getHireDate() != null)
                .filter(d -> {
                    LocalDate hireDate = LocalDate.parse(d.getHireDate());
                    return !hireDate.isBefore(today.minusYears(1));
                }).count();
        return EmployeeResponseDTO.EmployeeManagementResponseDto.builder()
                .summary(EmployeeResponseDTO.EmployeeSummaryDto.builder()
                        .totalCount(totalCount)
                        .workingCount(workingCount)
                        .vacationCount(vacationCount)
                        .newHireCount(newHireCount)
                        .build())
                .list(list)
                .build();
    }
}
