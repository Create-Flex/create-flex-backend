package com.mcn.in4.domain.department.service;

import com.mcn.in4.domain.department.dto.request.DepartmentRequest;
import com.mcn.in4.domain.department.dto.response.DepartmentDetailResponse;
import com.mcn.in4.domain.department.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> findAllDepartments();
    void createDepartment(DepartmentRequest request);
    void updateDepartment(Long id, DepartmentRequest request);
    void deleteDepartment(Long id);
    DepartmentDetailResponse findDepartmentDetail(Long id);

}