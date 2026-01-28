package com.mcn.in4.domain.department.service;

import com.mcn.in4.domain.department.dto.request.DepartmentRequest;
import com.mcn.in4.domain.department.dto.response.DepartmentResponse;
import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> findAllDepartments();
    void createDepartment(DepartmentRequest request); // 추가
    void updateDepartment(Long id, DepartmentRequest request); // 추가
    void deleteDepartment(Long id); // 추가
}