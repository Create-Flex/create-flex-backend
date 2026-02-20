package com.mcn.in4.domain.notification.service;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.notification.dto.NotificationDto;
import com.mcn.in4.domain.notification.entity.Notification;
import com.mcn.in4.domain.notification.repository.NotificationRepository;
import com.mcn.in4.global.sse.SseEmitters;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final SseEmitters sseEmitters;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    // 알림 저장 및 전송 (공통 메서드)
    private void saveAndSendNotification(Member member, String type, String title, String message, Object data) {
        String additionalData = null;
        if (data != null) {
            try {
                additionalData = objectMapper.writeValueAsString(data);
            } catch (Exception e) {
                System.err.println("Failed to serialize notification data: " + e.getMessage());
            }
        }

        Notification notificationEntity = Notification.builder()
                .member(member)
                .type(type)
                .title(title)
                .message(message)
                .additionalData(additionalData)
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notificationEntity);

        NotificationDto notificationDto = NotificationDto.builder()
                .notificationId(savedNotification.getNotificationId())
                .type(type)
                .title(title)
                .message(message)
                .data(data)
                .timestamp(savedNotification.getCreatedAt())
                .isRead(savedNotification.isRead())
                .build();

        sseEmitters.send(member.getMemberId(), "notification", notificationDto);
    }

    @Override
    public void sendLegalTaxRegistrationNotification(String managerName, String type, Object data) {
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);
        admins.forEach(admin -> saveAndSendNotification(admin, "LEGAL_TAX_REGISTERED", type + " 등록",
                managerName + "님이 " + type + "을(를) 등록했습니다.", data));
    }

    @Override
    public void sendLegalTaxApprovalNotification(Member manager, String creatorName, String type, Object data) {
        if (manager != null) {
            saveAndSendNotification(manager, "LEGAL_TAX_APPROVED", type + " 상담 완료",
                    creatorName + "님의 " + type + " 상담이 완료되었습니다.", data);
        }
    }

    @Override
    public void sendHealthSubmissionNotification(String memberName, String checkupName) {
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);
        admins.forEach(admin -> saveAndSendNotification(admin, "HEALTH_SUBMITTED", "건강검진 결과 제출",
                memberName + "님이 " + checkupName + " 건강검진 결과를 제출했습니다.", null));
    }

    @Override
    public void sendCreatorHealthSubmissionNotification(String creatorName, String checkupName, Long managerId) {
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);
        List<Long> adminIds = admins.stream().map(Member::getMemberId).collect(Collectors.toList());

        List<Member> targets = new ArrayList<>(admins);
        if (managerId != null && !adminIds.contains(managerId)) {
            memberRepository.findById(managerId).ifPresent(targets::add);
        }

        targets.forEach(member -> saveAndSendNotification(member, "CREATOR_HEALTH_SUBMITTED", "크리에이터 건강 정보 제출",
                creatorName + "님이 " + checkupName + "를 제출했습니다.", null));
    }

    @Override
    public void sendAdvertisementRegistrationNotification(String creatorName, String campaignName, Long creatorId,
            Long managerId, Long currentUserId) {
        List<Long> targetIds = new ArrayList<>(List.of(creatorId));
        if (managerId != null && !managerId.equals(creatorId)) {
            targetIds.add(managerId);
        }
        targetIds.removeIf(id -> id.equals(currentUserId));

        targetIds.forEach(id -> memberRepository.findById(id)
                .ifPresent(member -> saveAndSendNotification(member, "ADVERTISEMENT_REGISTERED", "광고 캠페인 등록",
                        creatorName + "님의 광고 캠페인 \"" + campaignName + "\"이(가) 등록되었습니다.", null)));
    }

    @Override
    public void sendAdvertisementAcceptanceNotification(String creatorName, String campaignName, Long creatorId,
            Long managerId, Long currentUserId) {
        List<Long> targetIds = new ArrayList<>(List.of(creatorId));
        if (managerId != null && !managerId.equals(creatorId)) {
            targetIds.add(managerId);
        }
        targetIds.removeIf(id -> id.equals(currentUserId));

        targetIds.forEach(id -> memberRepository.findById(id)
                .ifPresent(member -> saveAndSendNotification(member, "ADVERTISEMENT_ACCEPTED", "광고 캠페인 수락",
                        creatorName + "님의 광고 캠페인 \"" + campaignName + "\"이(가) 수락되었습니다.", null)));
    }

    @Override
    public void sendAdvertisementRejectionNotification(String creatorName, String campaignName, Long creatorId,
            Long managerId, Long currentUserId) {
        List<Long> targetIds = new ArrayList<>(List.of(creatorId));
        if (managerId != null && !managerId.equals(creatorId)) {
            targetIds.add(managerId);
        }
        targetIds.removeIf(id -> id.equals(currentUserId));

        targetIds.forEach(id -> memberRepository.findById(id)
                .ifPresent(member -> saveAndSendNotification(member, "ADVERTISEMENT_REJECTED", "광고 캠페인 거절",
                        creatorName + "님의 광고 캠페인 \"" + campaignName + "\"이(가) 거절되었습니다.", null)));
    }

    @Override
    public void sendScheduleRegistrationNotification(String senderName, String scheduleName, Long receiverId) {
        if (receiverId != null) {
            memberRepository.findById(receiverId).ifPresent(member -> saveAndSendNotification(member,
                    "SCHEDULE_REGISTERED", "일정 등록", senderName + "님이 일정 \"" + scheduleName + "\"을(를) 등록했습니다.", null));
        }
    }

    @Override
    public void sendVacationRequestNotification(String employeeName, String vacationType) {
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);
        admins.forEach(admin -> saveAndSendNotification(admin, "VACATION_REQUESTED", "휴가 신청",
                employeeName + "님이 " + vacationType + " 휴가를 신청했습니다.", null));
    }

    @Override
    public void sendVacationResultNotification(Long employeeId, String vacationType, String result) {
        if (employeeId != null) {
            memberRepository.findById(employeeId).ifPresent(member -> saveAndSendNotification(member, "VACATION_RESULT",
                    "휴가 " + result, vacationType + " 휴가 신청이 " + result + "되었습니다.", null));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return notificationRepository.findAllByMemberOrderByCreatedAtDesc(member).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        List<Notification> notifications = notificationRepository.findAllByMemberOrderByCreatedAtDesc(member);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    private NotificationDto convertToDto(Notification entity) {
        Object data = null;
        if (entity.getAdditionalData() != null) {
            try {
                data = objectMapper.readValue(entity.getAdditionalData(), String.class);
            } catch (Exception e) {
                try {
                    data = objectMapper.readValue(entity.getAdditionalData(), Object.class);
                } catch (Exception e2) {
                    System.err.println("Failed to deserialize notification data: " + e2.getMessage());
                }
            }
        }

        return NotificationDto.builder()
                .notificationId(entity.getNotificationId())
                .type(entity.getType())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .data(data)
                .timestamp(entity.getCreatedAt())
                .isRead(entity.isRead())
                .build();
    }
}
