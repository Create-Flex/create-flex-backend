package com.mcn.in4.domain.department.dto.response;

import com.mcn.in4.domain.department.entity.Department;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DepartmentResponse {
    private final Long departmentId;
    private final String departmentName;
    private final String departmentCall;
    private final String departmentDetail;
    private final String departmentColor;

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getDepartmentId(),
                department.getDepartmentName(),
                department.getDepartmentCall(),
                department.getDepartmentDetail(),
                department.getDepartmentColor()
        );
    }
}