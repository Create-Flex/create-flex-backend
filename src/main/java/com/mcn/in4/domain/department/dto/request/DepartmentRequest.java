package com.mcn.in4.domain.department.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DepartmentRequest {
    private String departmentName;
    private String departmentCall;
    private String departmentDetail;
    private String departmentColor;
}