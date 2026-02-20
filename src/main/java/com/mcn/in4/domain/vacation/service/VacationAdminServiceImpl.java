package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.notification.service.NotificationService;
import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationDetailResponseDTO;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import com.mcn.in4.domain.vacation.dto.response.VacationStatisticsResponseDTO;
import com.mcn.in4.domain.vacation.entity.VacationFamily;
import com.mcn.in4.domain.vacation.entity.VacationSick;
import com.mcn.in4.domain.vacation.entity.VacationWorkation;
import com.mcn.in4.domain.vacation.repository.VacationFamilyRepository;
import com.mcn.in4.domain.vacation.repository.VacationSickRepository;
import com.mcn.in4.domain.vacation.repository.VacationWorkationRepository;
import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import com.mcn.in4.domain.vacation.repository.VacationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        private final VacationFamilyRepository vacationFamilyRepository;
        private final VacationSickRepository vacationSickRepository;
        private final VacationWorkationRepository vacationWorkationRepository;
        private final NotificationService notificationService;

        /** 전체 휴가 목록 조회 (기간, 상태, 이름, 휴가유형 필터 적용) - 페이징 적용 */
        @Override
        public Page<AdminVacationListResponseDTO> getVacationList(
                        LocalDate startDate,
                        LocalDate endDate,
                        VacationApprove status,
                        String name,
                        VacationType type,
                        Pageable pageable) {
                // 미승인(APPROVE_NEED)의 경우 신청일 내림차순(최근 신청부터), 그 외는 시작일 내림차순
                Sort sort = (status == VacationApprove.APPROVE_NEED)
                                ? Sort.by(Sort.Direction.DESC, "vacationRequest")
                                : Sort.by(Sort.Direction.DESC, "vacationStart");

                Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

                Specification<Vacation> spec = VacationSpecification.filterVacation(startDate, endDate, status, name,
                                type);
                Page<Vacation> vacationPage = vacationRepository.findAll(spec, sortedPageable);

                // N+1 해결: memberIds 수집 후 한 번에 조회
                List<Long> memberIds = vacationPage.getContent().stream()
                                .map(v -> v.getMember().getMemberId())
                                .distinct()
                                .toList();

                Map<Long, Double> remainderMap = memberEmployeeDetailRepository
                                .findByMemberMemberIdIn(memberIds)
                                .stream()
                                .collect(Collectors.toMap(
                                        d -> d.getMember().getMemberId(),
                                        MemberEmployeeDetail::getVacationRemainder
                                ));

                return vacationPage.map(vacation -> {
                        Double remainder = remainderMap.getOrDefault(vacation.getMember().getMemberId(), 0.0);
                        return AdminVacationListResponseDTO.from(vacation, remainder);
                });
        }

        /** 휴가 승인 처리 (승인 대기 상태만 가능) */
        @Override
        @Transactional
        public void approveVacation(Long vacationId) {
                Vacation vacation = vacationRepository.findById(vacationId)
                                .orElseThrow(() -> new CustomException(ErrorCode.VACATION_NOT_FOUND,
                                                "휴가 ID " + vacationId + "를 찾을 수 없습니다."));

                if (vacation.getVacationApprove() != VacationApprove.APPROVE_NEED) {
                        throw new CustomException(ErrorCode.INVALID_VACATION_STATUS,
                                        "휴가 ID " + vacationId + "의 현재 상태는 " + vacation.getVacationApprove().name()
                                                        + "입니다. 승인대기(APPROVE_NEED) 상태의 휴가만 처리할 수 있습니다.");
                }

                vacation.approve();

                // 직원에게 알림 전송
                try {
                        notificationService.sendVacationResultNotification(
                                        vacation.getMember().getMemberId(),
                                        vacation.getVacationType().getDescription(),
                                        "승인");
                } catch (Exception e) {
                        // 알림 전송 실패해도 승인은 정상 처리
                }
        }

        /** 휴가 반려 처리 (연차/반차는 잔여일수 복구) */
        @Override
        @Transactional
        public void rejectVacation(Long vacationId, String rejectReason) {
                Vacation vacation = vacationRepository.findById(vacationId)
                                .orElseThrow(() -> new CustomException(ErrorCode.VACATION_NOT_FOUND,
                                                "휴가 ID " + vacationId + "를 찾을 수 없습니다."));

                if (vacation.getVacationApprove() != VacationApprove.APPROVE_NEED) {
                        throw new CustomException(ErrorCode.INVALID_VACATION_STATUS,
                                        "휴가 ID " + vacationId + "의 현재 상태는 " + vacation.getVacationApprove().name()
                                                        + "입니다. 승인대기(APPROVE_NEED) 상태의 휴가만 처리할 수 있습니다.");
                }

                // 연차/반차인 경우 잔여 연차 복구
                VacationType type = vacation.getVacationType();
                if (type == VacationType.ANNUAL || type == VacationType.HALF) {
                        MemberEmployeeDetail employeeDetail = memberEmployeeDetailRepository
                                        .findByMemberMemberId(vacation.getMember().getMemberId())
                                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_DETAIL_NOT_FOUND,
                                                        "회원 ID " + vacation.getMember().getMemberId()
                                                                        + "의 직원 상세 정보가 존재하지 않습니다."));

                        employeeDetail.increaseVacationRemainder(vacation.getVacationDays());
                }

                vacation.reject(rejectReason);

                // 직원에게 알림 전송
                try {
                        notificationService.sendVacationResultNotification(
                                        vacation.getMember().getMemberId(),
                                        vacation.getVacationType().getDescription(),
                                        "반려");
                } catch (Exception e) {
                        // 알림 전송 실패해도 반려는 정상 처리
                }
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
                                currentMonth.toString());
        }

        /** 휴가 상세 조회 (관리자용, 유형별 상세 정보 포함) */
        @Override
        public VacationDetailResponseDTO getVacationDetail(Long vacationId) {
                Vacation vacation = vacationRepository.findById(vacationId)
                                .orElseThrow(() -> new CustomException(ErrorCode.VACATION_NOT_FOUND,
                                                "휴가 ID " + vacationId + "를 찾을 수 없습니다."));

                // 타입별 상세 정보 조회
                return switch (vacation.getVacationType()) {
                        case FAMILY -> {
                                VacationFamily family = vacationFamilyRepository.findByVacationVacationId(vacationId)
                                                .orElse(null);
                                yield family != null
                                                ? VacationDetailResponseDTO.from(vacation, family)
                                                : VacationDetailResponseDTO.from(vacation);
                        }
                        case SICK -> {
                                VacationSick sick = vacationSickRepository.findByVacationVacationId(vacationId)
                                                .orElse(null);
                                yield sick != null
                                                ? VacationDetailResponseDTO.from(vacation, sick)
                                                : VacationDetailResponseDTO.from(vacation);
                        }
                        case WORKATION -> {
                                VacationWorkation workation = vacationWorkationRepository
                                                .findByVacationVacationId(vacationId)
                                                .orElse(null);
                                yield workation != null
                                                ? VacationDetailResponseDTO.from(vacation, workation)
                                                : VacationDetailResponseDTO.from(vacation);
                        }
                        default -> VacationDetailResponseDTO.from(vacation);
                };
        }
}
