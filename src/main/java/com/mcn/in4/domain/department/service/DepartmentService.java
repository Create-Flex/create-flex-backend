package com.mcn.in4.domain.department.service;

import com.mcn.in4.domain.department.dto.response.DepartmentResponse;
import java.util.List;

public interface DepartmentService {
    //전체 목록 부서 조회
    List<DepartmentResponse> findAllDepartments();
}