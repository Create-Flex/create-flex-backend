package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;

import java.time.LocalDate;
import java.util.List;

/**
 * 휴가 관리 서비스 인터페이스 (관리자용)
 * - 전체 휴가 목록 조회, 승인/반려 처리
 */
public interface VacationAdminService {

    /**
     * 전체 휴가 신청 목록 조회 (관리자용)
     * @param startDate 시작일 (필터)
     * @param endDate 종료일 (필터)
     * @param status 결재상태 (null이면 전체)
     * @param name 이름 검색 (null이면 전체)
     * @return 휴가 신청 목록
     */
    List<AdminVacationListResponseDTO> getVacationList(
            LocalDate startDate,
            LocalDate endDate,
            VacationApprove status,
            String name
    );

    /**
     * 휴가 승인
     * @param vacationId 휴가 ID
     */
    void approveVacation(Long vacationId);

    /**
     * 휴가 반려
     * @param vacationId 휴가 ID
     * @param rejectReason 반려 사유
     */
    void rejectVacation(Long vacationId, String rejectReason);
}
