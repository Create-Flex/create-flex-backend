package com.mcn.in4.domain.employee.service;

import com.mcn.in4.domain.employee.dto.requestDTO.EmployeeRequestDTO;
import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;

public interface EmployeeService {
    EmployeeResponseDTO.EmployeeDetailResponseDto getEmployeeDetail(Long id);

    // 직원 관리 리스트 및 요약 정보 조회
    EmployeeResponseDTO.EmployeeManagementResponseDto getEmployeeManagementList(String name);

    void registerEmployee(EmployeeRequestDTO.EmployeeInsertRequestDto requestDto);

    // 직원 퇴사 처리
    void quitEmployee(Long id, EmployeeRequestDTO.EmployeeQuitRequestDto requestDto);



}
