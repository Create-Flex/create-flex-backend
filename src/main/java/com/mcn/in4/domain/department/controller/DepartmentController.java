package com.mcn.in4.domain.department.controller;

import com.mcn.in4.domain.department.controller.api.DepartmentApi;
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
@RequestMapping("/api/admin/departments")
public class DepartmentController implements DepartmentApi {

    private final DepartmentService departmentService;

    @Override
    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentRequest request) {
        departmentService.createDepartment(request);
        return ResponseEntity.ok("부서가 성공적으로 생성되었습니다.");
    }

    @Override
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.findAllDepartments());
    }

    @Override
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentDetailResponse> getDepartmentDetail(@PathVariable("departmentId") Long departmentId) {
        return ResponseEntity.ok(departmentService.findDepartmentDetail(departmentId));
    }

    @Override
    @PatchMapping("/{departmentId}")
    public ResponseEntity<String> updateDepartment(
            @PathVariable("departmentId") Long departmentId,
            @RequestBody DepartmentRequest request) {

        departmentService.updateDepartment(departmentId, request);
        return ResponseEntity.ok("부서 정보가 성공적으로 수정되었습니다.");
    }

    @Override
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("departmentId") Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok("부서가 성공적으로 삭제되었습니다.");
    }
}