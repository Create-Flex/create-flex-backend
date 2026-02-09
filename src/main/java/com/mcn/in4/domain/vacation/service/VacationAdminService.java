package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationStatisticsResponseDTO;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * 휴가 관리 서비스 인터페이스 (관리자용)
 * - 전체 휴가 목록 조회, 승인/반려 처리
 */
public interface VacationAdminService {

    /**
     * 전체 휴가 신청 목록 조회 (관리자용) - 페이징 적용
     * @param startDate 시작일 (필터)
     * @param endDate 종료일 (필터)
     * @param status 결재상태 (null이면 전체)
     * @param name 이름 검색 (null이면 전체)
     * @param type 휴가유형 (null이면 전체)
     * @param pageable 페이징 정보
     * @return 휴가 신청 목록 (페이징)
     */
    Page<AdminVacationListResponseDTO> getVacationList(
            LocalDate startDate,
            LocalDate endDate,
            VacationApprove status,
            String name,
            VacationType type,
            Pageable pageable
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

    /**
     * 휴가 통계 조회
     * @return 이번달 휴가자 수, 미승인 대기자 수, 이번달 병가자 수
     */
    VacationStatisticsResponseDTO getVacationStatistics();
}
