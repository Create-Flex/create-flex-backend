package com.mcn.in4.domain.employee.service;

import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;

public interface EmployeeService {
    EmployeeResponseDTO.EmployeeDetailResponseDto getEmployeeDetail(Long id);
}
