package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    /** 전체 휴가 목록 조회 (기간, 상태, 이름 필터 적용) */
    @Override
    public List<AdminVacationListResponseDTO> getVacationList(
            LocalDate startDate,
            LocalDate endDate,
            VacationApprove status,
            String name
    ) {
        List<Vacation> vacations = vacationRepository.findAllWithFilters(startDate, endDate, status, name);

        return vacations.stream()
                .map(vacation -> {
                    // 잔여 연차 조회
                    Double remainder = memberEmployeeDetailRepository
                            .findByMemberMemberId(vacation.getMember().getMemberId())
                            .map(MemberEmployeeDetail::getVacationRemainder)
                            .orElse(0.0);
                    return AdminVacationListResponseDTO.from(vacation, remainder);
                })
                .collect(Collectors.toList());
    }

    /** 휴가 승인 처리 (승인 대기 상태만 가능) */
    @Override
    @Transactional
    public void approveVacation(Long vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 휴가입니다. vacationId: " + vacationId));

        if (vacation.getVacationApprove() != VacationApprove.APPROVE_NEED) {
            throw new IllegalArgumentException("승인 대기 상태의 휴가만 승인할 수 있습니다.");
        }

        vacation.approve();
    }

    /** 휴가 반려 처리 (연차/반차는 잔여일수 복구) */
    @Override
    @Transactional
    public void rejectVacation(Long vacationId, String rejectReason) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 휴가입니다. vacationId: " + vacationId));

        if (vacation.getVacationApprove() != VacationApprove.APPROVE_NEED) {
            throw new IllegalArgumentException("승인 대기 상태의 휴가만 반려할 수 있습니다.");
        }

        // 연차/반차인 경우 잔여 연차 복구
        VacationType type = vacation.getVacationType();
        if (type == VacationType.ANNUAL || type == VacationType.HALF) {
            MemberEmployeeDetail employeeDetail = memberEmployeeDetailRepository
                    .findByMemberMemberId(vacation.getMember().getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("직원 상세 정보가 없습니다."));

            employeeDetail.increaseVacationRemainder(vacation.getVacationDays());
        }

        vacation.reject(rejectReason);
    }
}
