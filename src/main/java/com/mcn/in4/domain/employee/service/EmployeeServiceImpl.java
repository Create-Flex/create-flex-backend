package com.mcn.in4.domain.employee.service;

import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.AttendanceStatus;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final MemberRepository memberRepository;// 유저 정보
    private final MemberEmployeeDetailRepository detailRepository;// 직원 상세
    private final AttendanceRepository attendanceRepository; // 근태
    private final VacationRepository vacationRepository; // 휴가 조회


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
        // 부서 정보를  페치 조인 으로 가져옴
        List<Member> members = memberRepository.findAllWithDepartment();
        // 회원 상세 정보를 ID 기반으로 찾음
        List<MemberEmployeeDetail> details = detailRepository.findAll();
        Map<Long, MemberEmployeeDetail> detailMap = details.stream()
                .collect(Collectors.toMap(d -> d.getMember().getMemberId(), d -> d));
        // 오늘 날짜의 모든 근태 기록을 가져옴
        List<Attendance> attendanceList = attendanceRepository.findAllByAttendanceDate(today);
        Map<Long, AttendanceStatus> attendanceStatusMap = attendanceList.stream()
                .collect(Collectors.toMap(a -> a.getMember().getMemberId(), Attendance::getAttendanceStatus));


        List<EmployeeResponseDTO.EmployeeListDto> list = members.stream().map(member -> {
            Long memberId = member.getMemberId();
            MemberEmployeeDetail detail = detailMap.get(memberId);

            //  Map에서 Enum을 먼저 꺼냄 없으면 null
            AttendanceStatus status = attendanceStatusMap.get(memberId);

            //  status가 있으면 설명을 가져오고 없으면 미출근 문자열 넣기
            String statusText = (status != null) ? status.getDescription() : "미출근";
            return EmployeeResponseDTO.EmployeeListDto.builder()
                    .memberName(member.getMemberName())
                    .departmentName(member.getDepartment() != null ? member.getDepartment().getDepartmentName() : "소속 없음")
                    .task(member.getTask())
                    .memberAccount(member.getMemberAccount())
                    .corporEmail(detail != null ? detail.getCorporEmail() : null)
                    .personalCall(detail != null ? detail.getPersonalCall() : null)
                    .hireDate(detail != null ? detail.getHireDate() : null)
                    .attendanceStatus(statusText) // String 타입
                    .build();
        }).collect(Collectors.toList());
        // 요약 통계 계산
        long totalCount = members.size();
        long workingCount = list.stream()
                .filter(e -> "출근".equals(e.getAttendanceStatus()) || "근무중".equals(e.getAttendanceStatus()))
                .count();
        long vacationCount = vacationRepository.countActiveVacations(today, VacationApprove.APPROVED);
        long newHireCount = details.stream()
                .filter(d -> d.getHireDate() != null)
                .filter(d -> !LocalDate.parse(d.getHireDate()).isBefore(today.minusYears(1)))
                .count();
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
