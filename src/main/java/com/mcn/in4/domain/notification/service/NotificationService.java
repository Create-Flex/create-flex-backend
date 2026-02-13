package com.mcn.in4.domain.notification.service;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.notification.dto.NotificationDto;
import com.mcn.in4.global.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 알림 서비스
// 실시간 알림 전송을 담당합니다.
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitters sseEmitters;
    private final MemberRepository memberRepository;

    // 법률/세무 등록 알림을 관리자에게 전송
    public void sendLegalTaxRegistrationNotification(String managerName, String type, Object data) {
        // ADMINISTRATOR 권한을 가진 모든 사용자 조회
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);

        if (admins.isEmpty()) {
            return;
        }

        // 알림 생성
        NotificationDto notification = NotificationDto.builder()
                .type("LEGAL_TAX_REGISTERED")
                .title(type + " 등록")
                .message(managerName + "님이 " + type + "을(를) 등록했습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        // 각 관리자에게 알림 전송
        List<Long> adminIds = admins.stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        sseEmitters.sendToMembers(adminIds, "notification", notification);
    }

    // 법률/세무 승인(완료) 알림을 담당 매니저에게 전송
    public void sendLegalTaxApprovalNotification(Member manager, String creatorName, String type, Object data) {
        if (manager == null) {
            return;
        }

        // 알림 생성
        NotificationDto notification = NotificationDto.builder()
                .type("LEGAL_TAX_APPROVED")
                .title(type + " 상담 완료")
                .message(creatorName + "님의 " + type + " 상담이 완료되었습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        // 매니저에게 알림 전송
        sseEmitters.send(manager.getMemberId(), "notification", notification);
    }

    // 특정 사용자에게 알림을 전송합니다.
    public void sendNotificationToMember(Long memberId, NotificationDto notification) {
        sseEmitters.send(memberId, "notification", notification);
    }

    // 여러 사용자에게 알림을 전송합니다.
    public void sendNotificationToMembers(List<Long> memberIds, NotificationDto notification) {
        sseEmitters.sendToMembers(memberIds, "notification", notification);
    }

    // 건강 검진 결과 제출 알림을 관리자에게 전송
    public void sendHealthSubmissionNotification(String memberName, String checkupName) {
        // ADMINISTRATOR 권한을 가진 모든 사용자 조회
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);

        if (admins.isEmpty()) {
            return;
        }

        // 알림 생성
        NotificationDto notification = NotificationDto.builder()
                .type("HEALTH_SUBMITTED")
                .title("건강검진 결과 제출")
                .message(memberName + "님이 " + checkupName + " 건강검진 결과를 제출했습니다.")
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        // 관리자에게 알림 전송
        List<Long> adminIds = admins.stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        sseEmitters.sendToMembers(adminIds, "notification", notification);
    }

    // 크리에이터 건강 관리 알림을 관리자, 담당 매니저에게 전송
    public void sendCreatorHealthSubmissionNotification(String creatorName, String checkupName, Long managerId) {
        // ADMINISTRATOR 권한을 가진 모든 사용자 조회
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);

        // 알림 대상 ID 리스트
        List<Long> targetIds = admins.stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        // 담당 매니저가 있으면 추가 (중복 방지)
        if (managerId != null && !targetIds.contains(managerId)) {
            targetIds.add(managerId);
        }

        if (targetIds.isEmpty()) {
            return;
        }

        // 알림 생성
        NotificationDto notification = NotificationDto.builder()
                .type("CREATOR_HEALTH_SUBMITTED")
                .title("크리에이터 건강 정보 제출")
                .message(creatorName + "님이 " + checkupName + "를 제출했습니다.")
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        // 관리자, 담당 매니저에게 알림 전송
        sseEmitters.sendToMembers(targetIds, "notification", notification);
    }

    // 광고 캠페인 등록 알림을 크리에이터, 담당 매니저에게 전송
    public void sendAdvertisementRegistrationNotification(String creatorName, String campaignName, Long creatorId,
            Long managerId, Long currentUserId) {
        List<Long> targetIds = new ArrayList<>(List.of(creatorId));

        // 담당 매니저가 있고 크리에이터와 다른 경우 추가
        if (managerId != null && !managerId.equals(creatorId)) {
            targetIds.add(managerId);
        }

        // 현재 사용자(등록한 사람)는 알림 대상에서 제외
        targetIds.removeIf(id -> id.equals(currentUserId));

        if (targetIds.isEmpty()) {
            return;
        }

        NotificationDto notification = NotificationDto.builder()
                .type("ADVERTISEMENT_REGISTERED")
                .title("광고 캠페인 등록")
                .message(creatorName + "님의 광고 캠페인 \"" + campaignName + "\"이(가) 등록되었습니다.")
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        sseEmitters.sendToMembers(targetIds, "notification", notification);
    }

    // 광고 캠페인 수락 알림을 크리에이터, 담당 매니저에게 전송
    public void sendAdvertisementAcceptanceNotification(String creatorName, String campaignName, Long creatorId,
            Long managerId, Long currentUserId) {
        List<Long> targetIds = new ArrayList<>(List.of(creatorId));

        // 담당 매니저가 있고 크리에이터와 다른 경우 추가
        if (managerId != null && !managerId.equals(creatorId)) {
            targetIds.add(managerId);
        }

        // 현재 사용자(수락한 사람)는 알림 대상에서 제외
        targetIds.removeIf(id -> id.equals(currentUserId));

        if (targetIds.isEmpty()) {
            return;
        }

        NotificationDto notification = NotificationDto.builder()
                .type("ADVERTISEMENT_ACCEPTED")
                .title("광고 캠페인 수락")
                .message(creatorName + "님의 광고 캠페인 \"" + campaignName + "\"이(가) 수락되었습니다.")
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        sseEmitters.sendToMembers(targetIds, "notification", notification);
    }

    // 광고 캠페인 거절 알림을 크리에이터, 담당 매니저에게 전송
    public void sendAdvertisementRejectionNotification(String creatorName, String campaignName, Long creatorId,
            Long managerId, Long currentUserId) {
        List<Long> targetIds = new ArrayList<>(List.of(creatorId));

        // 담당 매니저가 있고 크리에이터와 다른 경우 추가
        if (managerId != null && !managerId.equals(creatorId)) {
            targetIds.add(managerId);
        }

        // 현재 사용자(거절한 사람)는 알림 대상에서 제외
        targetIds.removeIf(id -> id.equals(currentUserId));

        if (targetIds.isEmpty()) {
            return;
        }

        NotificationDto notification = NotificationDto.builder()
                .type("ADVERTISEMENT_REJECTED")
                .title("광고 캠페인 거절")
                .message(creatorName + "님의 광고 캠페인 \"" + campaignName + "\"이(가) 거절되었습니다.")
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        sseEmitters.sendToMembers(targetIds, "notification", notification);
    }

    // 일정 등록 알림 전송 (매니저 <-> 크리에이터)
    public void sendScheduleRegistrationNotification(String senderName, String scheduleName, Long receiverId) {
        if (receiverId == null) {
            return;
        }

        NotificationDto notification = NotificationDto.builder()
                .type("SCHEDULE_REGISTERED")
                .title("일정 등록")
                .message(senderName + "님이 일정 \"" + scheduleName + "\"을(를) 등록했습니다.")
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        sseEmitters.send(receiverId, "notification", notification);
    }

    // 휴가 신청 알림 (직원 -> 관리자)
    public void sendVacationRequestNotification(String employeeName, String vacationType) {
        // ADMINISTRATOR 권한을 가진 모든 사용자 조회
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);

        if (admins.isEmpty()) {
            return;
        }

        NotificationDto notification = NotificationDto.builder()
                .type("VACATION_REQUESTED")
                .title("휴가 신청")
                .message(employeeName + "님이 " + vacationType + " 휴가를 신청했습니다.")
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        List<Long> adminIds = admins.stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        sseEmitters.sendToMembers(adminIds, "notification", notification);
    }

    // 휴가 처리 결과 알림 (관리자 -> 직원)
    public void sendVacationResultNotification(Long employeeId, String vacationType, String result) {
        if (employeeId == null) {
            return;
        }

        NotificationDto notification = NotificationDto.builder()
                .type("VACATION_RESULT")
                .title("휴가 " + result)
                .message(vacationType + " 휴가 신청이 " + result + "되었습니다.")
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        sseEmitters.send(employeeId, "notification", notification);
    }
}