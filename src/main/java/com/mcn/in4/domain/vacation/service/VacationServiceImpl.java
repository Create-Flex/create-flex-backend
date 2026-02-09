package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import com.mcn.in4.domain.vacation.dto.response.VacationDetailResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationRemainderResponseDTO;
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
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 휴가 서비스 구현체 (사용자용)
 * - 휴가 신청 시 연차/반차는 즉시 잔여 연차에서 차감
 * - 휴가 유형별 상세 정보는 별도 테이블에 저장
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacationServiceImpl implements VacationService {

    private final VacationRepository vacationRepository;
    private final VacationFamilyRepository vacationFamilyRepository;
    private final VacationSickRepository vacationSickRepository;
    private final VacationWorkationRepository vacationWorkationRepository;
    private final MemberRepository memberRepository;
    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;

    /** 휴가 신청 (연차/반차는 잔여일수 차감 후 저장) */
    @Override
    @Transactional
    public VacationResponseDTO createVacation(String type, VacationRequestDTO request, Long memberId) {
        // 휴가 타입 검증
        VacationType vacationType = parseVacationType(type);

        // 회원 조회 (토큰에서 추출한 memberId 사용)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, "회원 ID " + memberId + "를 찾을 수 없습니다."));

        // 반차인 경우 종료일을 시작일과 동일하게 설정
        LocalDate vacationEnd = (vacationType == VacationType.HALF)
                ? request.getVacationStart()
                : request.getVacationEnd();

        // 휴가 날짜 중복 확인 (반려 제외, 승인대기/승인됨만 확인)
        List<Vacation> overlappingVacations = vacationRepository.findOverlappingVacations(
                memberId, request.getVacationStart(), vacationEnd);

        if (!overlappingVacations.isEmpty()) {
            Vacation overlap = overlappingVacations.get(0);
            throw new CustomException(ErrorCode.VACATION_DATE_OVERLAP,
                    "기존 휴가(" + overlap.getVacationStart() + " ~ " + overlap.getVacationEnd() + ")와 날짜가 겹칩니다.");
        }

        // 휴가 일수 계산
        double vacationDays = calculateVacationDays(vacationType, request);

        // 연차/반차인 경우 잔여일수 확인 및 차감
        if (vacationType == VacationType.ANNUAL || vacationType == VacationType.HALF) {
            MemberEmployeeDetail employeeDetail = memberEmployeeDetailRepository.findByMemberMemberId(member.getMemberId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_DETAIL_NOT_FOUND, "회원 ID " + member.getMemberId() + "의 직원 상세 정보가 존재하지 않습니다."));

            if (employeeDetail.getVacationRemainder() < vacationDays) {
                throw new CustomException(ErrorCode.INSUFFICIENT_VACATION_REMAINDER,
                        "잔여 연차 " + employeeDetail.getVacationRemainder() + "일이지만 " + vacationDays + "일을 요청하셨습니다.");
            }

            // 잔여 연차 차감
            employeeDetail.decreaseVacationRemainder(vacationDays);
        }

        // 휴가 엔티티 생성
        Vacation vacation = Vacation.builder()
                .member(member)
                .vacationType(vacationType)
                .vacationStart(request.getVacationStart())
                .vacationEnd(vacationEnd)
                .vacationRequest(LocalDate.now())
                .vacationDetail(request.getVacationDetail())
                .vacationDays(vacationDays)
                .vacationApprove(VacationApprove.APPROVE_NEED)
                .build();

        Vacation savedVacation = vacationRepository.save(vacation);

        //타입별 상세 엔티티 저장
        switch (vacationType) {
            case FAMILY -> saveVacationFamily(savedVacation, request);
            case SICK -> saveVacationSick(savedVacation, request);
            case WORKATION -> saveVacationWorkation(savedVacation, request);
            default -> {} // ANNUAL, HALF는 상세 정보 없음
        }

        return VacationResponseDTO.from(savedVacation);
    }

    /** 휴가 일수 계산 (반차: 0.5일, 그 외: 시작일~종료일) */
    private double calculateVacationDays(VacationType type, VacationRequestDTO request) {
        if (type == VacationType.HALF) {
            return 0.5;
        }
        // 시작일 ~ 종료일 일수 계산 (종료일 포함)
        return ChronoUnit.DAYS.between(request.getVacationStart(), request.getVacationEnd()) + 1;
    }

    /** 휴가 타입 문자열을 enum으로 변환 */
    private VacationType parseVacationType(String type) {
        try {
            return VacationType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_VACATION_TYPE,
                    "'" + type + "'은(는) 유효하지 않은 휴가 타입입니다. (ANNUAL, HALF, FAMILY, SICK, WORKATION 중 하나를 입력해주세요.)");
        }
    }

    /** 경조사 휴가 상세 정보 저장 */
    private void saveVacationFamily(Vacation vacation, VacationRequestDTO request) {
        if (request.getFamilyRelation() == null || request.getFamilyDetail() == null) {
            throw new CustomException(ErrorCode.VACATION_DETAIL_REQUIRED,
                    "경조사(FAMILY) 휴가는 경조사 관계(familyRelation), 경조사 내용(familyDetail)이 필수입니다.");
        }

        VacationFamily vacationFamily = VacationFamily.builder()
                .vacation(vacation)
                .familyRelation(request.getFamilyRelation())
                .familyDetail(request.getFamilyDetail())
                .build();

        vacationFamilyRepository.save(vacationFamily);
    }

    /** 병가 휴가 상세 정보 저장 */
    private void saveVacationSick(Vacation vacation, VacationRequestDTO request) {
        if (request.getSickDetail() == null || request.getSickHospital() == null) {
            throw new CustomException(ErrorCode.VACATION_DETAIL_REQUIRED,
                    "병가(SICK) 휴가는 증상 내용(sickDetail), 진료 병원명(sickHospital)이 필수입니다.");
        }

        VacationSick vacationSick = VacationSick.builder()
                .vacation(vacation)
                .sickDetail(request.getSickDetail())
                .sickHospital(request.getSickHospital())
                .build();

        vacationSickRepository.save(vacationSick);
    }

    /** 워케이션 휴가 상세 정보 저장 */
    private void saveVacationWorkation(Vacation vacation, VacationRequestDTO request) {
        if (request.getWorkationWhere() == null || request.getWorkationContact() == null ||
                request.getWorkationPlan() == null || request.getWorkationHandover() == null) {
            throw new CustomException(ErrorCode.VACATION_DETAIL_REQUIRED,
                    "워케이션(WORKATION) 휴가는 근무 장소(workationWhere), 연락처(workationContact), 업무 계획(workationPlan), 업무 인계 내용(workationHandover)이 필수입니다.");
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

    /** 내 휴가 목록 조회 (기간, 유형 필터 적용) */
    @Override
    public List<VacationListResponseDTO> getMyVacations(Long memberId, LocalDate startDate, LocalDate endDate, VacationType type) {
        List<Vacation> vacations = vacationRepository.findMyVacationsWithFilters(memberId, startDate, endDate, type);

        return vacations.stream()
                .map(VacationListResponseDTO::from)
                .collect(Collectors.toList());
    }

    /** 휴가 상세 조회 (타입별 상세 정보 포함, 본인 휴가 또는 관리자만 조회 가능) */
    @Override
    public VacationDetailResponseDTO getVacationDetail(Long vacationId, Long memberId, boolean isAdmin) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new CustomException(ErrorCode.VACATION_NOT_FOUND, "휴가 ID " + vacationId + "를 찾을 수 없습니다."));

        // 관리자가 아니면 본인 휴가인지 확인
        if (!isAdmin && !vacation.getMember().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "휴가 ID " + vacationId + "에 대한 접근 권한이 없습니다.");
        }

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
                VacationWorkation workation = vacationWorkationRepository.findByVacationVacationId(vacationId)
                        .orElse(null);
                yield workation != null
                        ? VacationDetailResponseDTO.from(vacation, workation)
                        : VacationDetailResponseDTO.from(vacation);
            }
            default -> VacationDetailResponseDTO.from(vacation);
        };
    }

    /** 내 잔여 연차 조회 */
    @Override
    public VacationRemainderResponseDTO getMyVacationRemainder(Long memberId) {
        MemberEmployeeDetail employeeDetail = memberEmployeeDetailRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_DETAIL_NOT_FOUND, "회원 ID " + memberId + "의 직원 상세 정보가 존재하지 않습니다."));

        // 총 연차 15일 (기본값)
        double totalVacation = 15.0;

        return VacationRemainderResponseDTO.from(employeeDetail, totalVacation);
    }
}
