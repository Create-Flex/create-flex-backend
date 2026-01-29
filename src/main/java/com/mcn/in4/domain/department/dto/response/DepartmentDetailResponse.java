package com.mcn.in4.domain.department.dto.response;

import com.mcn.in4.domain.department.entity.Department;
import lombok.Getter;

import java.util.List;

@Getter
public class DepartmentDetailResponse {
    private final Long departmentId;
    private final String departmentName;
    private final List<MemberSummaryResponse> members; // 이미 변환된 리스트를 받음

    public DepartmentDetailResponse(Department department, List<MemberSummaryResponse> members) {
        this.departmentId = department.getDepartmentId();
        this.departmentName = department.getDepartmentName();
        this.members = members;
    }
}