package com.mcn.in4.domain.vacation.service;

import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationResponseDTO;

public interface VacationService {

    /**
     * 휴가 신청
     * @param type 휴가 타입 (ANNUAL, HALF, FAMILY, SICK, WORKATION)
     * @param request 휴가 신청 정보
     * @return 생성된 휴가 정보
     */
    VacationResponseDTO createVacation(String type, VacationRequestDTO request);
}
