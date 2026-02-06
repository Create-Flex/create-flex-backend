package com.mcn.in4.domain.employee.service;

import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.CheckInStatus;
import com.mcn.in4.domain.attendance.repository.AttendanceRepository;
import com.mcn.in4.domain.department.entity.Department;
import com.mcn.in4.domain.department.repository.DepartmentRepository;
import com.mcn.in4.domain.employee.dto.requestDTO.EmployeeRequestDTO;
import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
        private final MemberRepository memberRepository;// 유저 정보
        private final MemberEmployeeDetailRepository detailRepository;// 직원 상세
        private final AttendanceRepository attendanceRepository; // 근태
        private final VacationRepository vacationRepository; // 휴가 조회
        private final DepartmentRepository departmentRepository; // 부서 레포
        private final PasswordEncoder passwordEncoder; // 비밀번호 암호화

        // 직원 상세 조회
        @Override
        public EmployeeResponseDTO.EmployeeDetailResponseDto getEmployeeDetail(Long id) {
                // 계정 정보 조회
                Member member = memberRepository.findById(id)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                // 직원 상세 정보 조회
                MemberEmployeeDetail detail = detailRepository.findByMemberMemberId(id)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_DETAIL_NOT_FOUND));

                return EmployeeResponseDTO.EmployeeDetailResponseDto.builder()
                                .memberid(member.getMemberId())
                                .memberAccount(member.getMemberAccount())
                                .memberName(member.getMemberName())
                                .memberRole(member.getMemberRole())
                                .memberStatus(member.getMemberStatus())
                                .task(member.getTask())
                                // 부서가 있을 경우에만 아이디 추출
                                .departmentid(member.getDepartment() != null
                                                ? member.getDepartment().getDepartmentId().intValue()
                                                : null)

                                .nickname(detail.getNickname())
                                .personalEmail(detail.getPersonalEmail())
                                .personalCall(detail.getPersonalCall())
                                .address(detail.getAddress())
                                .engName(detail.getEngName())
                                .corporEmail(detail.getCorporEmail())
                                .hireDate(detail.getHireDate())
                                .employmentType(detail.getEmploymentType())
                                .build();
        }

        // 직원 리스트
        @Override
        public EmployeeResponseDTO.EmployeeManagementResponseDto getEmployeeManagementList(String name) {
                LocalDate today = LocalDate.now();
                // 부서 정보를 페치 조인 으로 가져옴
                List<Member> members = memberRepository.findAllWithDepartment();
                // 회원 상세 정보를 ID 기반으로 찾음
                List<MemberEmployeeDetail> details = detailRepository.findAll();
                Map<Long, MemberEmployeeDetail> detailMap = details.stream()
                                .collect(Collectors.toMap(d -> d.getMember().getMemberId(), d -> d));
                // 오늘 날짜의 모든 근태 기록을 가져옴
                List<Attendance> attendanceList = attendanceRepository.findAllByAttendanceDate(today);
                Map<Long, Attendance> attendanceMap = attendanceList.stream()
                                .collect(Collectors.toMap(a -> a.getMember().getMemberId(), a -> a));

                // 검색 필터 로직
                List<Member> filteredMembers = members;
                if (name != null && !name.trim().isEmpty()) {
                        filteredMembers = members.stream()
                                        .filter(m -> m.getMemberName().contains(name))
                                        .collect(Collectors.toList());
                }

                List<EmployeeResponseDTO.EmployeeListDto> list = filteredMembers.stream().map(member -> {
                        Long memberId = member.getMemberId();
                        MemberEmployeeDetail detail = detailMap.get(memberId);

                        // Map에서 Attendance를 먼저 꺼냄, 없으면 null
                        Attendance attendance = attendanceMap.get(memberId);

                        // 상태 텍스트 계산
                        String statusText;
                        if (attendance == null) {
                                statusText = "미출근";
                        } else if (attendance.getCheckOutStatus() == null) {
                                // 퇴근 기록이 없으면 근무중
                                statusText = "근무중";
                        } else {
                                // 출근 상태 표시
                                statusText = attendance.getCheckInStatus() != null
                                                ? attendance.getCheckInStatus().getDescription()
                                                : "출근";
                        }

                        return EmployeeResponseDTO.EmployeeListDto.builder()
                                        .memberid(memberId)
                                        .memberName(member.getMemberName())
                                        .departmentName(member.getDepartment() != null
                                                        ? member.getDepartment().getDepartmentName()
                                                        : "소속 없음")
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
                                .filter(e -> "출근".equals(e.getAttendanceStatus())
                                                || "근무중".equals(e.getAttendanceStatus())
                                                || "정상".equals(e.getAttendanceStatus()))
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

        // 직원 등록 기능
        @Override
        public void registerEmployee(EmployeeRequestDTO.EmployeeInsertRequestDto requestDto) {

                // 부서 정보 조회
                Department department = departmentRepository.findById(requestDto.getDepartmentid().longValue())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "존재하지 않는 부서입니다. ID: " + requestDto.getDepartmentid()));
                // Member 엔티티 생성 및 저장 (계정 정보)
                // 패스워드 인코더 없이 사번(memberAccount)으로 - 패스워드 인코더 기능 추후 보안
                Member member = Member.builder()
                                .memberAccount(requestDto.getMemberAccount())
                                .memberPassword(requestDto.getPassword()) // 현재 평문 저장
                                .memberName(requestDto.getMemberName())
                                .memberRole(requestDto.getMemberRole())
                                .memberStatus(requestDto.getMemberStatus())
                                .task(requestDto.getTask())
                                .department(department)
                                .build();
                Member savedMember = memberRepository.save(member);
                // MemberEmployeeDetail 저장
                MemberEmployeeDetail detail = MemberEmployeeDetail.builder()
                                .member(savedMember)
                                .nickname(requestDto.getNickname())
                                .personalEmail(requestDto.getPersonalEmail())
                                .personalCall(requestDto.getPersonalCall())
                                .address(requestDto.getAddress())
                                .engName(requestDto.getEngName())
                                .corporEmail(requestDto.getCorporEmail())
                                .hireDate(requestDto.getHireDate())
                                .employmentType(requestDto.getEmploymentType())
                                .vacationRemainder(15.0)
                                .build();
                detailRepository.save(detail);
        }

        @Override
        public void quitEmployee(Long id, EmployeeRequestDTO.EmployeeQuitRequestDto requestDto) {

                Member member = memberRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));
                log.info("직원 이 존재하는지 확인하기 위함 : 직원 퇴사 처리 기능");
                MemberEmployeeDetail detail = detailRepository.findByMemberMemberId(id)
                                .orElseThrow(() -> new IllegalArgumentException("직원 상세 정보가 없습니다."));
                log.info("직원 상세정보가 있는지 확인하기 위함 : 직원 퇴사 처리 기능");

                String today = LocalDate.now().toString();
                member.updateStatus(MemberStatus.SUSPENDED);
                detail.markAsQuit(today, requestDto.getLeavingReason());

                // 맴버의 상태 값을 바꿈 SUSPENDED
                memberRepository.save(member);
                // 퇴사 사유랑 일자 넣기
                detailRepository.save(detail);
        }

        @Override
        @Transactional
        public void updateEmployee(Long id, EmployeeRequestDTO.EmployeeUpdateRequestDto requestDto) {

                // 엔티티 조회
                Member member = memberRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));

                MemberEmployeeDetail detail = detailRepository.findByMemberMemberId(id)
                                .orElseThrow(() -> new IllegalArgumentException("직원 상세 정보가 없습니다."));
                // 부서 정보 조회 (부서 변경이 필요할 수 있으므로)
                Department department = null;
                if (requestDto.getDepartmentid() != null) {
                        department = departmentRepository.findById(requestDto.getDepartmentid().longValue())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "존재하지 않는 부서입니다. ID: " + requestDto.getDepartmentid()));
                }
                // Member 엔티티 수정
                member.updateInfo(
                                requestDto.getMemberName(),
                                requestDto.getMemberRole(),
                                requestDto.getMemberStatus(),
                                requestDto.getTask(),
                                department);
                // 비밀번호 수정 (입력값이 있을 때만)
                if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {

                        member.updatePassword(requestDto.getPassword());
                }
                // MemberEmployeeDetail 엔티티 수정
                detail.updateDetail(
                                requestDto.getNickname(),
                                requestDto.getPersonalEmail(),
                                requestDto.getPersonalCall(),
                                requestDto.getAddress(),
                                requestDto.getEngName(),
                                requestDto.getCorporEmail(),
                                requestDto.getHireDate(),
                                requestDto.getEmploymentType());

        }

        // 마이페이지 프로필 수정 (본인용)
        @Override
        @Transactional
        public EmployeeResponseDTO.EmployeeDetailResponseDto updateMyProfile(Long memberId,
                        EmployeeRequestDTO.MyProfileUpdateDto request) {
                // 1. 회원 조회
                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                // 2. 직원 상세 정보 조회
                MemberEmployeeDetail detail = detailRepository.findByMember(member)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_DETAIL_NOT_FOUND));

                // 3. 정보 업데이트
                // 3. 정보 업데이트
                detail.updateDetail(
                                request.getNickname() != null ? request.getNickname() : detail.getNickname(),
                                request.getPersonalEmail() != null ? request.getPersonalEmail()
                                                : detail.getPersonalEmail(),
                                request.getPersonalCall() != null ? request.getPersonalCall()
                                                : detail.getPersonalCall(),
                                request.getAddress() != null ? request.getAddress() : detail.getAddress(),
                                request.getEngName() != null ? request.getEngName() : detail.getEngName(),
                                detail.getCorporEmail(), // 기존 값 유지
                                detail.getHireDate(), // 기존 값 유지
                                detail.getEmploymentType() // 기존 값 유지
                );

                // 4. 업데이트된 정보 반환
                return getEmployeeDetail(memberId);
        }

        // 비밀번호 변경 (본인용)
        @Override
        @Transactional
        public void changePassword(Long memberId, EmployeeRequestDTO.PasswordChangeDto request) {
                // 1. 회원 조회
                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                // 2. 현재 비밀번호 검증 (평문 비교 - 기존 로그인 로직과 동일하게)
                if (!request.getCurrentPassword().equals(member.getMemberPassword())) {
                        throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
                }

                // 새로운 비밀번호가 기존 비밀번호와 같은지, 같다면 에러
                if (request.getNewPassword().equals(member.getMemberPassword())) {
                        throw new CustomException(ErrorCode.SAME_PASSWORD);
                }

                // 3. 새 비밀번호 업데이트 (평문 저장)
                member.updatePassword(request.getNewPassword());
                memberRepository.save(member);
        }
}
