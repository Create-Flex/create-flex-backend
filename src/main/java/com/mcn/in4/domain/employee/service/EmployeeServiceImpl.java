package com.mcn.in4.domain.employee.service;

import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.CheckOutStatus;
import com.mcn.in4.domain.attendance.repository.AttendanceRepository;
import com.mcn.in4.domain.department.entity.Department;
import com.mcn.in4.domain.department.repository.DepartmentRepository;
import com.mcn.in4.domain.employee.dto.requestDTO.EmployeeRequestDTO;
import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.entity.MemberProfile;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberProfileRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
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
        private final MemberProfileRepository memberProfileRepository; // 프로필 레포
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
        @Transactional(readOnly = true)
        public EmployeeResponseDTO.EmployeeManagementResponseDto getEmployeeManagementList(String name, Pageable pageable) {
                LocalDate today = LocalDate.now();

            //  멤버 페이징 조회 (CREATOR 제외 로직 적용)
            Page<Member> memberPage;
            if (name != null && !name.trim().isEmpty()) {
                // 이름 검색 시 CREATOR 제외
                memberPage = memberRepository.searchByNameAndRoleNot(name.trim(), MemberRole.CREATOR, pageable);
            } else {
                // 전체 조회 시 CREATOR 제외
                memberPage = memberRepository.findByMemberRoleNot(MemberRole.CREATOR, pageable);
            }

            List<Member> members = memberPage.getContent();

            // 조회된 회원들의 ID 리스트 추출
            List<Long> memberIds = members.stream()
                    .map(Member::getMemberId)
                    .collect(Collectors.toList());

            // 연관 데이터 일괄 조회 (Batch Fetching)
            Map<Long, MemberEmployeeDetail> detailMap = new HashMap<>();
            Map<Long, Attendance> attendanceMap = new HashMap<>();

            if (!memberIds.isEmpty()) {
                // ID 리스트로 한 번에 조회 후 Map 변환
                List<MemberEmployeeDetail> details = detailRepository.findByMemberMemberIdIn(memberIds);
                detailMap = details.stream()
                        .collect(Collectors.toMap(d -> d.getMember().getMemberId(), d -> d));

                // ID 리스트로 한 번에 조회 후 Map 변환
                List<Attendance> attendances = attendanceRepository.findByAttendanceDateAndMemberMemberIdIn(today, memberIds);
                attendanceMap = attendances.stream()
                        .collect(Collectors.toMap(a -> a.getMember().getMemberId(), a -> a));
            }

            Map<Long, MemberEmployeeDetail> finalDetailMap = detailMap;
            Map<Long, Attendance> finalAttendanceMap = attendanceMap;

            List<EmployeeResponseDTO.EmployeeListDto> list = members.stream().map(member -> {
                Long memberId = member.getMemberId();

                MemberEmployeeDetail detail = finalDetailMap.get(memberId);
                Attendance attendance = finalAttendanceMap.get(memberId);

                // 상태 텍스트 계산
                String statusText;
                if (attendance == null) {
                    statusText = "미출근";
                } else if (attendance.getCheckOutStatus() == null) {
                    statusText = "근무중";
                } else {
                    statusText = attendance.getCheckOutStatus() == CheckOutStatus.NORMAL ? "정상" : attendance.getCheckOutStatus().getDescription();
                }

                return EmployeeResponseDTO.EmployeeListDto.builder()
                        .memberid(memberId)
                        .memberName(member.getMemberName())
                        .departmentName(member.getDepartment() != null ? member.getDepartment().getDepartmentName() : "소속 없음")
                        .task(member.getTask())
                        .memberAccount(member.getMemberAccount())
                        .corporEmail(detail != null ? detail.getCorporEmail() : null)
                        .personalCall(detail != null ? detail.getPersonalCall() : null)
                        .hireDate(detail != null ? detail.getHireDate() : null)
                        .attendanceStatus(statusText)
                        .build();
            }).collect(Collectors.toList());

            // 전체 사원 수 (CREATOR 제외)
            long totalCount = memberRepository.countByMemberRoleNot(MemberRole.CREATOR);

            // 오늘 출근한 전체 인원 계산 (근무중 + 퇴근자 포함)
            List<Attendance> allTodayAttendances = attendanceRepository.findAllByAttendanceDate(today);
            long workingCount = allTodayAttendances.size(); // 오늘 출근 기록이 있으면 일단 '근무 가능 인원'으로 간주

            // 휴가 중인 인원 (기존 방식 유지)
            long vacationCount = vacationRepository.countActiveVacations(today, VacationApprove.APPROVED);

            // 신규 입사자 (1년 이내 입사자 전체 카운트)
            List<MemberEmployeeDetail> allDetails = detailRepository.findAll();
            long newHireCount = allDetails.stream()
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
                    .pageInfo(EmployeeResponseDTO.PageInfo.builder() // 프론트엔드용 페이징 정보
                            .currentPage(memberPage.getNumber() + 1) // 1페이지부터 시작
                            .totalPages(memberPage.getTotalPages())
                            .totalElements(memberPage.getTotalElements())
                            .size(memberPage.getSize())
                            .build())
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
                Member member = Member.builder()
                                .memberAccount(requestDto.getMemberAccount())
                                .memberPassword(passwordEncoder.encode(requestDto.getPassword()))
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

                // 기본 프로필 생성 및 저장
                MemberProfile profile = MemberProfile.builder()
                                .member(savedMember)
                                .profileImage("https://i.postimg.cc/bJSGpBqg/Gemini-Generated-Image-s33rl9s33rl9s33r-(1).png")
                                .profileBanner("https://i.postimg.cc/mrjxhLg1/photo_1497366216548_37526070297c.avif")
                                .build();
                memberProfileRepository.save(profile);
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

                        member.updatePassword(passwordEncoder.encode(requestDto.getPassword()));
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
                // 회원 조회
                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                // 직원 상세 정보 조회
                MemberEmployeeDetail detail = detailRepository.findByMember(member)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_DETAIL_NOT_FOUND));

                // 정보 업데이트
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

                //업데이트된 정보 반환
                return getEmployeeDetail(memberId);
        }

        // 비밀번호 변경 (본인용)
        @Override
        @Transactional
        public void changePassword(Long memberId, EmployeeRequestDTO.PasswordChangeDto request) {
                // 회원 조회
                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                // 현재 비밀번호 검증
                if (!passwordEncoder.matches(request.getCurrentPassword(), member.getMemberPassword())) {
                        throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
                }

                // 새로운 비밀번호가 기존 비밀번호와 같은지, 같다면 에러
                if (passwordEncoder.matches(request.getNewPassword(), member.getMemberPassword())) {
                        throw new CustomException(ErrorCode.SAME_PASSWORD);
                }

                // 새 비밀번호 업데이트
                member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
                memberRepository.save(member);
        }
}
