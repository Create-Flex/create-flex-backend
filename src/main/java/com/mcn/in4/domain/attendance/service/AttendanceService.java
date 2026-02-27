package com.mcn.in4.domain.attendance.service;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.dto.AttendanceDashboardDto;
import com.mcn.in4.domain.attendance.dto.CompanyAttendanceDashboardDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AttendanceService {
        /**
         * 특정 직원의 근태 기록 조회
         * 
         * @param memberId  직원 ID
         * @param startDate 조회 시작 날짜
         * @param endDate   조회 종료 날짜
         * @param status    근태 상태 필터
         * @param pageable  페이징 정보
         * @return 근태 기록 Page
         */
        Page<AttendanceResponseDto> getAttendance(Long memberId, LocalDate startDate, LocalDate endDate,
                        String status, Pageable pageable);

        /**
         * 전체 직원의 근태 기록 조회
         * 
         * @param startDate 조회 시작 날짜
         * @param endDate   조회 종료 날짜
         * @param status    근태 상태 필터
         * @param name      이름 검색
         * @param pageable  페이징 정보
         * @return 근태 기록 Page
         */
        Page<AttendanceResponseDto> getAllAttendance(LocalDate startDate, LocalDate endDate,
                        String status, String name, Pageable pageable);

        /**
         * 출근 처리
         *
         * @param memberId 직원 ID
         * @return 서버에 저장된 attendanceStart (프론트 타이머 기준값으로 사용)
         */
        LocalDateTime checkIn(Long memberId);

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