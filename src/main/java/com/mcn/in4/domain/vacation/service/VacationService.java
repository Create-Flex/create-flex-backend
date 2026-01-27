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
import com.mcn.in4.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacationService {

    private final VacationRepository vacationRepository;
    private final VacationFamilyRepository vacationFamilyRepository;
    private final VacationSickRepository vacationSickRepository;
    private final VacationWorkationRepository vacationWorkationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public VacationResponseDTO createVacation(String type, VacationRequestDTO request) {
        // 1. 휴가 타입 검증
        VacationType vacationType = parseVacationType(type);

        // 2. 회원 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId: " + request.getMemberId()));

        // 3. 휴가 엔티티 생성
        Vacation vacation = Vacation.builder()
                .member(member)
                .vacationType(vacationType)
                .vacationStart(request.getVacationStart())
                .vacationEnd(request.getVacationEnd())
                .vacationRequest(LocalDate.now())
                .vacationDetail(request.getVacationDetail())
                .vacationApprove(VacationApprove.APPROVE_NEED)
                .build();

        Vacation savedVacation = vacationRepository.save(vacation);

        // 4. 타입별 상세 엔티티 저장
        switch (vacationType) {
            case FAMILY -> saveVacationFamily(savedVacation, request);
            case SICK -> saveVacationSick(savedVacation, request);
            case WORKATION -> saveVacationWorkation(savedVacation, request);
            default -> {} // ANNUAL, HALF는 상세 정보 없음
        }

        return VacationResponseDTO.from(savedVacation);
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
