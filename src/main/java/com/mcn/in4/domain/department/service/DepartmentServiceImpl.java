package com.mcn.in4.domain.department.service;

import com.mcn.in4.domain.department.dto.request.DepartmentRequest;
import com.mcn.in4.domain.department.dto.response.DepartmentResponse;
import com.mcn.in4.domain.department.entity.Department;
import com.mcn.in4.domain.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(DepartmentResponse::from)
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
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 부서가 존재하지 않습니다. id=" + id));
        departmentRepository.delete(department);
    }
}