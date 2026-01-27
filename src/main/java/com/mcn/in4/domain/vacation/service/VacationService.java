package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationResponseDTO;
import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.VacationFamily;
import com.mcn.in4.domain.vacation.entity.VacationSick;
import com.mcn.in4.domain.vacation.entity.VacationWorkation;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.vacation.repository.VacationFamilyRepository;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import com.mcn.in4.domain.vacation.repository.VacationSickRepository;
import com.mcn.in4.domain.vacation.repository.VacationWorkationRepository;
import com.mcn.in4.entity.member.Member;
import com.mcn.in4.entity.member.MemberEmployeeDetail;
import com.mcn.in4.entity.member.MemberEmployeeDetailRepository;
import com.mcn.in4.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacationService {

    private final VacationRepository vacationRepository;
    private final VacationFamilyRepository vacationFamilyRepository;
    private final VacationSickRepository vacationSickRepository;
    private final VacationWorkationRepository vacationWorkationRepository;
    private final MemberRepository memberRepository;
    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;

    @Transactional
    public VacationResponseDTO createVacation(String type, VacationRequestDTO request) {
        // 1. 휴가 타입 검증
        VacationType vacationType = parseVacationType(type);

        // 2. 회원 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId: " + request.getMemberId()));

        // 3. 휴가 일수 계산
        double vacationDays = calculateVacationDays(vacationType, request);

        // 4. 연차/반차인 경우 잔여일수 확인 및 차감
        if (vacationType == VacationType.ANNUAL || vacationType == VacationType.HALF) {
            MemberEmployeeDetail employeeDetail = memberEmployeeDetailRepository.findByMemberMemberId(member.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("직원 상세 정보가 없습니다. memberId: " + member.getMemberId()));

            if (employeeDetail.getVacationRemainder() < vacationDays) {
                throw new IllegalArgumentException(
                        String.format("잔여 연차가 부족합니다. 잔여: %.1f일, 신청: %.1f일",
                                employeeDetail.getVacationRemainder(), vacationDays));
            }

            // 잔여 연차 차감
            employeeDetail.decreaseVacationRemainder(vacationDays);
        }

        // 5. 휴가 엔티티 생성
        Vacation vacation = Vacation.builder()
                .member(member)
                .vacationType(vacationType)
                .vacationStart(request.getVacationStart())
                .vacationEnd(request.getVacationEnd())
                .vacationRequest(LocalDate.now())
                .vacationDetail(request.getVacationDetail())
                .vacationDays(vacationDays)
                .vacationApprove(VacationApprove.APPROVE_NEED)
                .build();

        Vacation savedVacation = vacationRepository.save(vacation);

        // 6. 타입별 상세 엔티티 저장
        switch (vacationType) {
            case FAMILY -> saveVacationFamily(savedVacation, request);
            case SICK -> saveVacationSick(savedVacation, request);
            case WORKATION -> saveVacationWorkation(savedVacation, request);
            default -> {} // ANNUAL, HALF는 상세 정보 없음
        }

        return VacationResponseDTO.from(savedVacation);
    }

    private double calculateVacationDays(VacationType type, VacationRequestDTO request) {
        if (type == VacationType.HALF) {
            return 0.5;
        }
        // 시작일 ~ 종료일 일수 계산 (종료일 포함)
        return ChronoUnit.DAYS.between(request.getVacationStart(), request.getVacationEnd()) + 1;
    }

    private VacationType parseVacationType(String type) {
        try {
            return VacationType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 휴가 타입입니다: " + type);
        }
    }

    private void saveVacationFamily(Vacation vacation, VacationRequestDTO request) {
        if (request.getFamilyRelation() == null || request.getFamilyDetail() == null) {
            throw new IllegalArgumentException("경조사 휴가는 대상(관계)과 경조내용이 필수입니다.");
        }

        VacationFamily vacationFamily = VacationFamily.builder()
                .vacation(vacation)
                .familyRelation(request.getFamilyRelation())
                .familyDetail(request.getFamilyDetail())
                .build();

        vacationFamilyRepository.save(vacationFamily);
    }

    private void saveVacationSick(Vacation vacation, VacationRequestDTO request) {
        if (request.getSickDetail() == null || request.getSickHospital() == null) {
            throw new IllegalArgumentException("병가는 증상 및 사유와 진료예정병원이 필수입니다.");
        }

        VacationSick vacationSick = VacationSick.builder()
                .vacation(vacation)
                .sickDetail(request.getSickDetail())
                .sickHospital(request.getSickHospital())
                .build();

        vacationSickRepository.save(vacationSick);
    }

    private void saveVacationWorkation(Vacation vacation, VacationRequestDTO request) {
        if (request.getWorkationWhere() == null || request.getWorkationContact() == null ||
                request.getWorkationPlan() == null || request.getWorkationHandover() == null) {
            throw new IllegalArgumentException("워케이션은 근무장소, 비상연락망, 업무계획, 업무인계사항이 필수입니다.");
        }

        VacationWorkation vacationWorkation = VacationWorkation.builder()
                .vacation(vacation)
                .workationWhere(request.getWorkationWhere())
                .workationContact(request.getWorkationContact())
                .workationPlan(request.getWorkationPlan())
                .workationHandover(request.getWorkationHandover())
                .build();

        vacationWorkationRepository.save(vacationWorkation);
    }
}
