package com.mcn.in4.domain.attendance.service;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.dto.AttendanceDashboardDto;
import com.mcn.in4.domain.attendance.dto.CompanyAttendanceDashboardDto;

import java.util.List;
import java.time.LocalDate;

/**
 * 근태 관리 서비스 인터페이스
 * 근태 조회 및 출퇴근 처리를 위한 비즈니스 로직을 정의합니다.
 */
public interface AttendanceService {
        /**
         * 특정 직원의 근태 기록 조회
         * 
         * @param memberId  직원 ID
         * @param startDate 조회 시작 날짜
         * @param endDate   조회 종료 날짜
         * @param status    근태 상태 필터
         * @return 근태 기록 DTO 리스트
         */
        List<AttendanceResponseDto> getAttendance(Long memberId, LocalDate startDate, LocalDate endDate,
                        String status);

        /**
         * 전체 직원의 근태 기록 조회
         * 
         * @param startDate 조회 시작 날짜
         * @param endDate   조회 종료 날짜
         * @param status    근태 상태 필터
         * @return 근태 기록 DTO 리스트
         */
        List<AttendanceResponseDto> getAllAttendance(LocalDate startDate, LocalDate endDate,
                        String status, String name);

        /**
         * 출근 처리
         * 
         * @param memberId 직원 ID
         */
        void checkIn(Long memberId);

        /**
         * 퇴근 처리
         * 
         * @param memberId 직원 ID
         */
        void checkOut(Long memberId);

        /**
         * 내 근태 통계 조회
         * 이번 달의 지각 횟수와 초과 근무 횟수를 조회합니다.
         * 
         * @param memberId 직원 ID
         * @return 대시보드 통계 DTO
         */
        AttendanceDashboardDto getMyDashboardStats(Long memberId);

        /**
         * 전사 근태 통계 조회
         * 전사 평균 출퇴근/근무 시간 및 오늘 근태 현황을 조회합니다.
         *
         * @return 전사 대시보드 통계 DTO
         */
        CompanyAttendanceDashboardDto getCompanyDashboardStats();
}