package com.mcn.in4.domain.employee.controller.api;

import com.mcn.in4.domain.employee.dto.requestDTO.EmployeeRequestDTO;
import com.mcn.in4.domain.employee.dto.responseDTO.EmployeeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Employee 관리", description = "직원 등록, 상세 조회 및 퇴사 처리를 담당하는 API입니다.")
public interface EmployeeApi {

        @Operation(summary = "직원 리스트 및 통계 조회", description = "이름 검색 조건을 포함하여 직원 목록과 전체 통계를 조회합니다.")
        ResponseEntity<EmployeeResponseDTO.EmployeeManagementResponseDto> getEmployeeManagementList(
                @Parameter(description = "검색할 직원 이름") String name);

        @Operation(summary = "직원 상세 조회", description = "특정 직원의 ID를 통해 상세 정보를 조회합니다.")
        ResponseEntity<EmployeeResponseDTO.EmployeeDetailResponseDto> getEmployeeDetail(
                @Parameter(description = "조회할 직원 ID", example = "1008") Long id);

        @Operation(summary = "직원 등록", description = "새로운 직원의 계정 정보와 인사 상세 정보를 등록합니다.", responses = {
                @ApiResponse(responseCode = "200", description = "직원 등록 성공"),
                @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
        })
        ResponseEntity<String> registerEmployee(
                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "신규 직원 정보", content = @Content(examples = @ExampleObject(value = "{\n  \"memberAccount\": \"IT1123\",\n  \"memberName\": \"박신입\",\n  \"memberRole\": \"EMPLOYEE\",\n  \"memberStatus\": \"WORKING\",\n  \"task\": \"백엔드 개발 및 유지보수\",\n  \"departmentid\": 1005,\n  \"password\": \"1234\",\n  \"nickname\": \"new_dev\",\n  \"personalEmail\": \"park.new@gmail.com\",\n  \"personalCall\": \"010-9999-8888\",\n  \"address\": \"경기도 성남시 분당구 판교로 123\",\n  \"engName\": \"Park New\",\n  \"corporEmail\": \"park.new@techcorp.com\",\n  \"hireDate\": \"2026-02-01\",\n  \"employmentType\": \"NEWBIE\"\n}"))) EmployeeRequestDTO.EmployeeInsertRequestDto requestDto);

        @Operation(summary = "직원 퇴사 처리", description = "직원의 상태를 변경하고 퇴사 사유를 기록합니다.", responses = {
                @ApiResponse(responseCode = "200", description = "퇴사 처리 성공"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 직원")
        })
        ResponseEntity<String> quitEmployee(
                @Parameter(description = "퇴사 처리할 직원 ID", example = "1008") Long id,
                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "퇴사 사유 정보", content = @Content(examples = @ExampleObject(value = "{\n  \"leavingReason\": \"성격이 이상합니다.\"\n}"))) EmployeeRequestDTO.EmployeeQuitRequestDto requestDto);


        @Operation(summary = "직원 정보 수정", description = "직원의 기본 정보 및 상세 정보를 수정합니다.")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = String.class))),
                @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음")
        })
        ResponseEntity<String> updateEmployee(
                @Parameter(description = "직원 ID", required = true, example = "1005") @PathVariable("id") Long id,
                @RequestBody(description = "수정할 직원 정보", content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = EmployeeRequestDTO.EmployeeUpdateRequestDto.class),
                        examples = @ExampleObject(
                                name = "직원 수정 예시",
                                value = """
                                    {
                                      "memberName": "김수정",
                                      "memberRole": "MANAGER",
                                      "memberStatus": "WORKING",
                                      "task": "매니저",
                                      "departmentid": 1003,
                                      "nickname": "Crystal",
                                      "personalEmail": "crystal.kim@naver.com",
                                      "personalCall": "010-9876-5432",
                                      "address": "서울시 마포구 연남동",
                                      "engName": "Crystal Kim",
                                      "corporEmail": "crystal@mcn.com",
                                      "hireDate": "2023-05-01",
                                      "employmentType": "EXPERIENCED"
                                    }
                                    """
                        )
                )) EmployeeRequestDTO.EmployeeUpdateRequestDto requestDto
        );
}