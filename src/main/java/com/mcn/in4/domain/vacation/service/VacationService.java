package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationDetailResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationRemainderResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationResponseDTO;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;

import java.time.LocalDate;
import java.util.List;

/**
 * 휴가 서비스 인터페이스 (사용자용)
 * - 휴가 신청, 내 휴가 목록/상세 조회, 잔여 연차 조회
 */
public interface VacationService {

    /**
     * 휴가 신청
     * @param type 휴가 타입 (ANNUAL, HALF, FAMILY, SICK, WORKATION)
     * @param request 휴가 신청 정보
     * @return 생성된 휴가 정보
     */
    VacationResponseDTO createVacation(String type, VacationRequestDTO request);

    /**
     * 내 휴가 사용 내역 목록 조회
     * @param memberId 회원 ID
     * @param startDate 시작일 필터
     * @param endDate 종료일 필터
     * @param type 휴가 유형 필터 (null이면 전체)
     * @return 휴가 목록
     */
    List<VacationListResponseDTO> getMyVacations(Long memberId, LocalDate startDate, LocalDate endDate, VacationType type);

    /**
     * 휴가 상세 조회
     * @param vacationId 휴가 ID
     * @return 휴가 상세 정보 (타입별 상세 포함)
     */
    VacationDetailResponseDTO getVacationDetail(Long vacationId);

    /**
     * 내 잔여 연차 조회
     * @param memberId 회원 ID
     * @return 잔여 연차 정보
     */
    VacationRemainderResponseDTO getMyVacationRemainder(Long memberId);
}
