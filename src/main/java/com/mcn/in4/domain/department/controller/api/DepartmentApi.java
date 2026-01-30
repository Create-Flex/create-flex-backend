package com.mcn.in4.domain.department.controller.api;

import com.mcn.in4.domain.department.dto.request.DepartmentRequest;
import com.mcn.in4.domain.department.dto.response.DepartmentDetailResponse;
import com.mcn.in4.domain.department.dto.response.DepartmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "부서 관리 (관리자)", description = "관리자 전용 부서 관리 API (생성, 조회, 수정, 삭제)")
public interface DepartmentApi {

    @Operation(summary = "부서 생성", description = "새로운 부서를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<String> createDepartment(
            @RequestBody DepartmentRequest request
    );

    @Operation(summary = "전체 부서 목록 조회", description = "등록된 모든 부서의 기본 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    ResponseEntity<List<DepartmentResponse>> getAllDepartments();

    @Operation(summary = "부서 상세 정보 조회", description = "부서 ID를 통해 부서의 상세 정보와 소속된 직원 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 부서 ID", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<DepartmentDetailResponse> getDepartmentDetail(
            @Parameter(description = "조회할 부서의 ID", example = "1")
            @PathVariable("departmentId") Long departmentId
    );

    @Operation(summary = "부서 정보 수정", description = "특정 부서의 이름이나 설명을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 부서", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<String> updateDepartment(
            @Parameter(description = "수정할 부서의 ID", example = "1")
            @PathVariable("departmentId") Long departmentId,
            @RequestBody DepartmentRequest request
    );

    @Operation(summary = "부서 삭제", description = "특정 부서를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 부서", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "소속된 직원이 있어 삭제 불가", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<String> deleteDepartment(
            @Parameter(description = "삭제할 부서의 ID", example = "1")
            @PathVariable("departmentId") Long departmentId
    );
}