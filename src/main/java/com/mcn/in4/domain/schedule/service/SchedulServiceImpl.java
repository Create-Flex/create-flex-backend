package com.mcn.in4.domain.schedule.service;

import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.schedule.dto.responseDTO.SchedulReponseDTO;
import com.mcn.in4.domain.schedule.dto.resquestDTO.ScheduleRequestDTO;
import com.mcn.in4.domain.schedule.entity.Schedule;
import com.mcn.in4.domain.schedule.entity.ScheduleVisitor;
import com.mcn.in4.domain.schedule.entity.scheduleEnum.ScheduleType;
import com.mcn.in4.domain.schedule.repository.SchedulRepository;
import com.mcn.in4.domain.schedule.repository.ScheduleVisitorRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import com.mcn.in4.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulServiceImpl implements SchedulService {
        private final SchedulRepository schedulRepository;
        private final MemberRepository memberRepository;
        private final CreatorDetailRepository creatorDetailRepository;
        private final ScheduleVisitorRepository scheduleVisitorRepository;
        private final NotificationService notificationService;

        @Override
        @Transactional
        public void createSchedule(Long memberId, String role, ScheduleRequestDTO.ScheduleCreateRequestDto requestDto) {

                // 어드민 외 회사일정 등록 불가
                if (!"ROLE_ADMINISTRATOR".equals(role) &&
                                requestDto.getScheduleType() == ScheduleType.COMPANY) {
                        throw new IllegalStateException("일반 직원 및 크리에이터는 회사 일정을 등록할 수 있는 권한이 없습니다.");
                }

                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
                log.info("회원 이 유효한지 조회");
                Member targetCreator = null;
                if (requestDto.getCreatorId() != null) {
                        targetCreator = memberRepository.findById(requestDto.getCreatorId())
                                        .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND));
                }
                log.info("크리에이터가 유효한지 조회");

                // 일반 일정 등록
                Schedule schedule = Schedule.builder()
                                .member(member)
                                .creator(targetCreator)
                                .scheduleName(requestDto.getScheduleName())
                                .scheduleDate(requestDto.getScheduleDate())
                                .scheduleDetail(requestDto.getScheduleDetail())
                                .scheduleType(requestDto.getScheduleType())
                                .build();
                Schedule savedSchedule = schedulRepository.save(schedule);

                // 합방 타입일 경우에 합방 크리에이터 등록
                if (requestDto.getScheduleType() == ScheduleType.MERGE &&
                                requestDto.getVisitorIds() != null && !requestDto.getVisitorIds().isEmpty()) {

                        List<ScheduleVisitor> visitors = requestDto.getVisitorIds().stream()
                                        .map(visitorId -> memberRepository.findById(visitorId)
                                                        .orElseThrow(() -> new IllegalArgumentException(
                                                                        "참가자 정보를 찾을 수 없습니다. ID: " + visitorId)))
                                        .map(visitorMember -> ScheduleVisitor.builder()
                                                        .schedule(savedSchedule)
                                                        .member(visitorMember)
                                                        .build())
                                        .toList();

                        scheduleVisitorRepository.saveAll(visitors);
                }

                // 알림 전송 로직
                try {
                        if (targetCreator != null && !targetCreator.getMemberId().equals(memberId)) {
                                // 매니저가 크리에이터의 일정을 등록한 경우 -> 크리에이터에게 알림
                                notificationService.sendScheduleRegistrationNotification(
                                                member.getMemberName(),
                                                savedSchedule.getScheduleName(),
                                                targetCreator.getMemberId());
                        } else {
                                // 본인이 등록한 경우 -> 담당 매니저에게 알림
                                // 작성자의 담당 매니저 찾기
                                Long managerId = creatorDetailRepository
                                                .findByCreatorIdWithManager(member.getMemberId())
                                                .map(detail -> detail.getMemberManager() != null
                                                                ? detail.getMemberManager().getMemberId()
                                                                : null)
                                                .orElse(null);

                                if (managerId != null) {
                                        notificationService.sendScheduleRegistrationNotification(
                                                        member.getMemberName(),
                                                        savedSchedule.getScheduleName(),
                                                        managerId);
                                }
                        }
                } catch (Exception e) {
                        log.error("일정 등록 알림 전송 실패", e);
                }
        }

        @Override
        @Transactional(readOnly = true)
        public List<SchedulReponseDTO.ScheduleResponseDto> getMyMonthlySchedules(Long memberId, String month) {
                // month: "YYYY-MM" 년-월
                String[] parts = month.split("-");
                int year = Integer.parseInt(parts[0]);
                int monthValue = Integer.parseInt(parts[1]);

                // 한달
                LocalDate startDate = LocalDate.of(year, monthValue, 1);
                LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

                List<Schedule> schedules = schedulRepository.findMyMonthlySchedules(
                                memberId,
                                startDate,
                                endDate,
                                ScheduleType.COMPANY,
                                ScheduleType.PERSONAL);

                return schedules.stream()
                                .map(s -> SchedulReponseDTO.ScheduleResponseDto.builder()
                                                .scheduleId(s.getScheduleId())
                                                .scheduleName(s.getScheduleName())
                                                .scheduleDate(s.getScheduleDate())
                                                .scheduleDetail(s.getScheduleDetail())
                                                .scheduleType(s.getScheduleType())
                                                .build())
                                .toList();

        }

        // 어드민 권한 체크 후 일정 삭제
        @Override
        public void deleteSchedule(Long memberId, String role, Long scheduleId) {

                Schedule schedule = schedulRepository.findById(scheduleId)
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

                boolean isAdmin = "ROLE_ADMINSTRATOR".equals(role)
                                && schedule.getScheduleType() == ScheduleType.COMPANY;

                boolean isOwner = schedule.getMember().getMemberId().equals(memberId);

                if (!isAdmin && !isOwner) {
                        throw new IllegalStateException("삭제 권한이 없습니다. (본인의 일정 또는 관리자의 회사 일정 삭제만 가능)");
                }

                schedulRepository.delete(schedule);
                log.info("알림 삭제 완료");

        }

        // 일정 수정 로직, 권한 체크, 작성자 체크
        @Override
        @Transactional
        public void updateSchedule(Long memberId, String role, Long scheduleId,
                        ScheduleRequestDTO.ScheduleUpdateRequestDto requestDto) {
                Schedule schedule = schedulRepository.findById(scheduleId)
                                .orElseThrow(() -> new IllegalArgumentException("일정이 없습니다."));
                boolean isAdminManager = "ROLE_ADMINISTRATOR".equals(role)
                                && schedule.getScheduleType() == ScheduleType.COMPANY;
                boolean isOwner = schedule.getMember().getMemberId().equals(memberId);

                if (!isAdminManager && !isOwner) {
                        throw new IllegalStateException("수정 권한이 없습니다.");
                }

                // 기존, 업데이트 타입
                ScheduleType oldType = schedule.getScheduleType();
                ScheduleType newType = requestDto.getScheduleType();

                schedule.update(
                                requestDto.getScheduleName(),
                                requestDto.getScheduleDate(),
                                requestDto.getScheduleDetail(),
                                requestDto.getScheduleType());
                // 합방 관리 로직
                // 기존 합방 -> 다른 타입 변경시
                if (oldType == ScheduleType.MERGE && newType != ScheduleType.MERGE) {
                        scheduleVisitorRepository.deleteAllBySchedule(schedule);
                        // 합방 테이블에 데이터 삭제
                } else if (newType == ScheduleType.MERGE) {
                        // 합방 타입으로 수정하는 경우
                        scheduleVisitorRepository.deleteAllBySchedule(schedule);
                        if (requestDto.getVisitorIds() != null && !requestDto.getVisitorIds().isEmpty()) {
                                List<ScheduleVisitor> newVisitors = requestDto.getVisitorIds().stream()
                                                .map(visitorId -> memberRepository.findById(visitorId)
                                                                .orElseThrow(() -> new CustomException(
                                                                                ErrorCode.CREATOR_NOT_FOUND)))
                                                .map(member -> ScheduleVisitor.builder()
                                                                .schedule(schedule)
                                                                .member(member)
                                                                .build())
                                                .toList();
                                scheduleVisitorRepository.saveAll(newVisitors);
                        }
                }

                log.info("일정 수정 완료");
        }

        // 크리에이터 일정 조회
        @Override
        public List<SchedulReponseDTO.ScheduleResponseDto> getCreatorSchedules(Long memberId, String role,
                        String month) {
                String[] parts = month.split("-");
                LocalDate startDate = LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 1);
                LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

                List<ScheduleType> creatorTypes = List.of(ScheduleType.CONTENT,
                                ScheduleType.LIVE,
                                ScheduleType.MEETING,
                                ScheduleType.MERGE,
                                ScheduleType.PROMOTION,
                                ScheduleType.ETC);

                // 매니저인 경우 크리에이터 목록 조회
                List<Long> managedCreatorIds = List.of();
                if ("ROLE_MANAGER".equals(role)) {
                        managedCreatorIds = creatorDetailRepository.findCreatorIdsByManagerId(memberId);
                }

                List<Schedule> schedules = schedulRepository.findCreatorRelatedSchedules(
                                memberId,
                                managedCreatorIds.isEmpty() ? List.of(-1L) : managedCreatorIds, // 빈리스트 일때 -1L 을 임의로 넣어
                                                                                                // 오류 방지
                                startDate, endDate, creatorTypes);

                // 합방 참가자 조회
                List<Long> scheduleIds = schedules.stream().map(Schedule::getScheduleId).toList();
                List<ScheduleVisitor> allVisitors = scheduleVisitorRepository.findAllByScheduleIdIn(scheduleIds);

                // <스케줄 id, 합방 참가자리스트> <- 맵
                Map<Long, List<ScheduleVisitor>> visitorMap = allVisitors.stream()
                                .collect(Collectors.groupingBy(sv -> sv.getSchedule().getScheduleId()));

                return schedules.stream().map(s -> {
                        List<ScheduleVisitor> visitors = visitorMap.getOrDefault(s.getScheduleId(), List.of());

                        return SchedulReponseDTO.ScheduleResponseDto.builder()
                                        .scheduleId(s.getScheduleId())
                                        .memberId(s.getMember().getMemberId())
                                        .memberName(s.getMember().getMemberName())
                                        .memberRole(s.getMember().getMemberRole())
                                        .scheduleName(s.getScheduleName())
                                        .scheduleDate(s.getScheduleDate())
                                        .scheduleDetail(s.getScheduleDetail())
                                        .scheduleType(s.getScheduleType())
                                        // 크리에이터
                                        .creatorId(s.getCreator() != null ? s.getCreator().getMemberId() : null)
                                        .creatorName(s.getCreator() != null ? s.getCreator().getMemberName() : null)
                                        // 합방 상대
                                        .visitorIds(visitors.stream().map(v -> v.getMember().getMemberId()).toList())
                                        .visitorNames(visitors.stream().map(v -> v.getMember().getMemberName())
                                                        .toList())
                                        .build();
                }).toList();

        }
}
