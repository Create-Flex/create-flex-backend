package com.mcn.in4.domain.department.service;

import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.repository.AttendanceRepository;
import com.mcn.in4.domain.department.dto.request.DepartmentRequest;
import com.mcn.in4.domain.department.dto.response.DepartmentDetailResponse;
import com.mcn.in4.domain.department.dto.response.DepartmentResponse;
import com.mcn.in4.domain.department.dto.response.MemberSummaryResponse;
import com.mcn.in4.domain.department.entity.Department;
import com.mcn.in4.domain.department.repository.DepartmentRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(dept -> DepartmentResponse.from(
                        dept,
                        memberRepository.countByDepartment_DepartmentId(
                                dept.getDepartmentId())))
                .toList();
    }

    @Override
    public void createDepartment(DepartmentRequest request) {
        Department department = new Department(
                null, // IDENTITY 전략이므로 null 전달
                request.getDepartmentName(),
                request.getDepartmentCall(),
                request.getDepartmentDetail(),
                request.getDepartmentColor()
        );
        departmentRepository.save(department);
    }

    @Override
    public void updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 부서가 존재하지 않습니다. id=" + id));

        department.update(
                request.getDepartmentName(),
                request.getDepartmentCall(),
                request.getDepartmentDetail(),
                request.getDepartmentColor()
        );
    }

    @Override
    public void deleteDepartment(Long id) {
        //삭제할 부서 조회
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 부서가 존재하지 않습니다. id=" + id));

        //해당 부서에 소속된 직원들을 조회
        List<Member> members = memberRepository.findByDepartment_DepartmentId(id);

        //직원들의 부서 정보를 null로 변경 (연관관계 끊기)
        for (Member member : members) {
            member.changeDepartment(null); // 방금 추가한 메서드 사용
        }
        //부서 삭제
        departmentRepository.delete(department);
    }

    @Override
    public DepartmentDetailResponse findDepartmentDetail(Long id) {
        // 1. 부서 정보 조회
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("부서 없음"));

        // 2. 해당 부서의 멤버들 조회
        List<Member> members = memberRepository.findByDepartment_DepartmentId(id);

        if (members.isEmpty()) {
            return new DepartmentDetailResponse(department, List.of());
        }

        // 3. memberIds 수집
        List<Long> memberIds = members.stream()
                .map(Member::getMemberId)
                .toList();

        LocalDate today = LocalDate.now();

        // 4. EmployeeDetail 한 번에 조회 → Map 변환 (N+1 해결)
        Map<Long, MemberEmployeeDetail> detailMap = memberEmployeeDetailRepository
                .findByMemberMemberIdIn(memberIds)
                .stream()
                .collect(Collectors.toMap(
                        d -> d.getMember().getMemberId(),
                        Function.identity()
                ));

        // 5. Attendance 한 번에 조회 → Map 변환 (N+1 해결)
        Map<Long, Attendance> attendanceMap = attendanceRepository
                .findByAttendanceDateAndMemberMemberIdIn(today, memberIds)
                .stream()
                .collect(Collectors.toMap(
                        a -> a.getMember().getMemberId(),
                        Function.identity()
                ));

        // 6. Map에서 조회하여 DTO 생성
        List<MemberSummaryResponse> memberSummaryList = members.stream()
                .map(member -> {
                    MemberEmployeeDetail detail = detailMap.get(member.getMemberId());
                    Attendance attendance = attendanceMap.get(member.getMemberId());
                    return new MemberSummaryResponse(member, detail, attendance);
                })
                .toList();

        return new DepartmentDetailResponse(department, memberSummaryList);
    }


}