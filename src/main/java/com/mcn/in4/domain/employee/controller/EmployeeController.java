package com.mcn.in4.domain.employee.controller;

import com.mcn.in4.domain.employee.controller.api.EmployeeApi;
import com.mcn.in4.domain.employee.dto.requestDTO.EmployeeRequestDTO;
import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;
import com.mcn.in4.domain.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {
    private final EmployeeService employeeService;

    // 직원 관리 리스트 및 통계 조회
    @Override
    @GetMapping("/")
    public ResponseEntity<EmployeeResponseDTO.EmployeeManagementResponseDto> getEmployeeManagementList(
            @RequestParam(value = "name", required = false) String name) {
        EmployeeResponseDTO.EmployeeManagementResponseDto response = employeeService.getEmployeeManagementList(name);
        return ResponseEntity.ok(response);
    }

    // 직원 상세 조회
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO.EmployeeDetailResponseDto> getEmployeeDetail(
            @PathVariable("id") Long id) {
        EmployeeResponseDTO.EmployeeDetailResponseDto response = employeeService.getEmployeeDetail(id);
        return ResponseEntity.ok(response);
    }

    // 직원 등록
    @Override
    @PostMapping("/insert")
    public ResponseEntity<String> registerEmployee(
            @RequestBody EmployeeRequestDTO.EmployeeInsertRequestDto requestDto) {
        employeeService.registerEmployee(requestDto);
        return ResponseEntity.ok("직원 등록이 완료되었습니다.");
    }

    // 직원 퇴사 처리
    @Override
    @PostMapping("/quit/{id}")
    public ResponseEntity<String> quitEmployee(
            @PathVariable("id") Long id,
            @RequestBody EmployeeRequestDTO.EmployeeQuitRequestDto requestDto) {

        employeeService.quitEmployee(id, requestDto);
        return ResponseEntity.ok("퇴사 처리가 완료되었습니다.");
    }
    // 직원 정보 수정
    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateEmployee(
            @PathVariable("id") Long id,
            @RequestBody EmployeeRequestDTO.EmployeeUpdateRequestDto requestDto) {
        employeeService.updateEmployee(id, requestDto);
        return ResponseEntity.ok("직원 정보가 수정되었습니다.");
    }


}
