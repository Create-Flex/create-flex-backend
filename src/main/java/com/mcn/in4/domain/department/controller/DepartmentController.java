package com.mcn.in4.domain.department.controller;

import com.mcn.in4.domain.department.dto.request.DepartmentRequest;
import com.mcn.in4.domain.department.dto.response.DepartmentDetailResponse;
import com.mcn.in4.domain.department.dto.response.DepartmentResponse;
import com.mcn.in4.domain.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DepartmentController {

    private final DepartmentService departmentService;

    // 전체 조회
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.findAllDepartments());
    }

    // 부서 추가
    @PostMapping("/departments")
    public ResponseEntity<Void> createDepartment(@RequestBody DepartmentRequest request) {
        departmentService.createDepartment(request);
        return ResponseEntity.ok().build();
    }

    // 부서 상세 수정
    @PatchMapping("/department/{id}")
    public ResponseEntity<Void> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRequest request) {
        departmentService.updateDepartment(id, request);
        return ResponseEntity.ok().build();
    }

    // 부서 삭제
    @DeleteMapping("/department/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

    // 부서 상세 조회
    @GetMapping("/department/{id}")
    public ResponseEntity<DepartmentDetailResponse> getDepartmentDetail(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.findDepartmentDetail(id));
    }
}