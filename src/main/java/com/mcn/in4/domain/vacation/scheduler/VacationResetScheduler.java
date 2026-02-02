package com.mcn.in4.domain.vacation.scheduler;

import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 연차 초기화 스케줄러
 * - 매년 1월 1일 00:00:00에 모든 직원의 연차를 15일로 초기화
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VacationResetScheduler {

    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;

    @Scheduled(cron = "0 0 0 1 1 *")
    @Transactional
    public void resetAnnualVacation() {
        log.info("연차 초기화 스케줄러 실행 시작");

        memberEmployeeDetailRepository.findAll()
                .forEach(emp -> emp.resetVacationRemainder());

        log.info("연차 초기화 스케줄러 실행 완료");
    }
}
