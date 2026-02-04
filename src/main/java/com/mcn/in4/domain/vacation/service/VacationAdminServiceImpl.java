package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import com.mcn.in4.domain.vacation.dto.response.VacationStatisticsResponseDTO;
import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 휴가 관리 서비스 구현체 (관리자용)
 * - 반려 시 연차/반차의 경우 잔여 연차 복구
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacationAdminServiceImpl implements VacationAdminService {

    private final VacationRepository vacationRepository;
    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;

    /** 전체 휴가 목록 조회 (기간, 상태, 이름, 휴가유형 필터 적용) */
    @Override
    public List<AdminVacationListResponseDTO> getVacationList(
            LocalDate startDate,
            LocalDate endDate,
            VacationApprove status,
            String name,
            VacationType type
    ) {
        List<Vacation> vacations = vacationRepository.findAllWithFilters(startDate, endDate, status, name, type);

        return vacations.stream()
                .map(vacation -> {
                    // 잔여 연차 조회
                    Double remainder = memberEmployeeDetailRepository
                            .findByMemberMemberId(vacation.getMember().getMemberId())
                            .map(MemberEmployeeDetail::getVacationRemainder)// 값이 있으면 : double(12.0), 없으면 : empty
                            .orElse(0.0); //값이 있으면 12.0, 없으면 0.0
                    return AdminVacationListResponseDTO.from(vacation, remainder);
                })
                .collect(Collectors.toList());
    }

    /** 휴가 승인 처리 (승인 대기 상태만 가능) */
    @Override
    @Transactional
    public void approveVacation(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new CustomException(ErrorCode.VACATION_NOT_FOUND));

        if (vacation.getVacationApprove() != VacationApprove.APPROVE_NEED) {
            throw new CustomException(ErrorCode.INVALID_VACATION_STATUS);
        }

        vacation.approve();
    }

    /** 휴가 반려 처리 (연차/반차는 잔여일수 복구) */
    @Override
    @Transactional
    public void rejectVacation(Long vacationId, String rejectReason) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new CustomException(ErrorCode.VACATION_NOT_FOUND));

        if (vacation.getVacationApprove() != VacationApprove.APPROVE_NEED) {
            throw new CustomException(ErrorCode.INVALID_VACATION_STATUS);
        }

        // 연차/반차인 경우 잔여 연차 복구
        VacationType type = vacation.getVacationType();
        if (type == VacationType.ANNUAL || type == VacationType.HALF) {
            MemberEmployeeDetail employeeDetail = memberEmployeeDetailRepository
                    .findByMemberMemberId(vacation.getMember().getMemberId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_DETAIL_NOT_FOUND));

            employeeDetail.increaseVacationRemainder(vacation.getVacationDays());
        }

        vacation.reject(rejectReason);
    }

    /** 휴가 통계 조회 (이번달 휴가자, 미승인 대기자, 이번달 병가자) */
    @Override
    public VacationStatisticsResponseDTO getVacationStatistics() {
        // 이번달 기준 날짜 계산
        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        // 이번달 휴가자 수 (승인된 휴가)
        long monthlyVacationCount = vacationRepository.countMonthlyVacations(
                monthStart, monthEnd, VacationApprove.APPROVED);

        // 총 미승인 대기자 수
        long pendingApprovalCount = vacationRepository.countByApproveStatus(VacationApprove.APPROVE_NEED);

        // 이번달 병가자 수 (승인된 병가)
        long monthlySickLeaveCount = vacationRepository.countMonthlyByType(
                monthStart, monthEnd, VacationType.SICK, VacationApprove.APPROVED);

        return VacationStatisticsResponseDTO.of(
                monthlyVacationCount,
                pendingApprovalCount,
                monthlySickLeaveCount,
                currentMonth.toString()
        );
    }
}
