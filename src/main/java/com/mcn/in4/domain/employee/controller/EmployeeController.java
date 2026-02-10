package com.mcn.in4.domain.employee.controller;

import com.mcn.in4.domain.employee.controller.api.EmployeeApi;
import com.mcn.in4.domain.employee.dto.requestDTO.EmployeeRequestDTO;
import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;
import com.mcn.in4.domain.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {
    private final EmployeeService employeeService;

    // 직원 관리 리스트 및 통계 조회
//@Override
    @GetMapping("/")
    public ResponseEntity<EmployeeResponseDTO.EmployeeManagementResponseDto> getEmployeeManagementList(
            @RequestParam(value = "name", required = false) String name,
            @PageableDefault(size = 10) Pageable pageable) {
        EmployeeResponseDTO.EmployeeManagementResponseDto response = employeeService.getEmployeeManagementList(name, pageable);
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

    // 직원 정보 수정 (관리자용)
    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateEmployee(
            @PathVariable("id") Long id,
            @RequestBody EmployeeRequestDTO.EmployeeUpdateRequestDto requestDto) {
        employeeService.updateEmployee(id, requestDto);
        return ResponseEntity.ok("직원 정보가 수정되었습니다.");
    }

    // 마이페이지 프로필 수정 (본인용 - JWT 토큰에서 ID 추출)
    @PatchMapping("/me")
    public ResponseEntity<EmployeeResponseDTO.EmployeeDetailResponseDto> updateMyProfile(
            @AuthenticationPrincipal String userId,
            @RequestBody EmployeeRequestDTO.MyProfileUpdateDto request) {
        Long memberId = Long.parseLong(userId);
        EmployeeResponseDTO.EmployeeDetailResponseDto response = employeeService.updateMyProfile(memberId, request);
        return ResponseEntity.ok(response);
    }

    // 비밀번호 변경 (본인용 - JWT 토큰에서 ID 추출)
    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal String userId,
            @RequestBody EmployeeRequestDTO.PasswordChangeDto request) {
        Long memberId = Long.parseLong(userId);
        employeeService.changePassword(memberId, request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

}
