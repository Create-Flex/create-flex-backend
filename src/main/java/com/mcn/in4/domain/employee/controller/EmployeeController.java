package com.mcn.in4.domain.employee.controller;

import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;
import com.mcn.in4.domain.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    // 직원 관리 리스트 및 통계 조회
    @GetMapping("/")
    public ResponseEntity<EmployeeResponseDTO.EmployeeManagementResponseDto> getEmployeeManagementList() {
        EmployeeResponseDTO.EmployeeManagementResponseDto response = employeeService.getEmployeeManagementList();
        return ResponseEntity.ok(response);
    }

    //직원 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO.EmployeeDetailResponseDto> getEmployeeDetail(@PathVariable("id") Long id) {
        EmployeeResponseDTO.EmployeeDetailResponseDto response = employeeService.getEmployeeDetail(id);
        return ResponseEntity.ok(response);
    }



}
