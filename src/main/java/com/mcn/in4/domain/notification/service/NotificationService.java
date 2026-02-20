package com.mcn.in4.domain.notification.service;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.notification.dto.NotificationDto;
import java.util.List;

public interface NotificationService {

    // 법률/세무 등록 알림을 관리자에게 전송
    void sendLegalTaxRegistrationNotification(String managerName, String type, Object data);

    // 법률/세무 승인(완료) 알림을 담당 매니저에게 전송
    void sendLegalTaxApprovalNotification(Member manager, String creatorName, String type, Object data);

    // 건강 검진 결과 제출 알림을 관리자에게 전송
    void sendHealthSubmissionNotification(String memberName, String checkupName);

    // 크리에이터 건강 관리 알림을 관리자, 담당 매니저에게 전송
    void sendCreatorHealthSubmissionNotification(String creatorName, String checkupName, Long managerId);

    // 광고 캠페인 등록 알림을 크리에이터, 담당 매니저에게 전송
    void sendAdvertisementRegistrationNotification(String creatorName, String campaignName, Long creatorId,
            Long managerId, Long currentUserId);

    // 광고 캠페인 수락 알림을 크리에이터, 담당 매니저에게 전송
    void sendAdvertisementAcceptanceNotification(String creatorName, String campaignName, Long creatorId,
            Long managerId, Long currentUserId);

    // 광고 캠페인 거절 알림을 크리에이터, 담당 매니저에게 전송
    void sendAdvertisementRejectionNotification(String creatorName, String campaignName, Long creatorId, Long managerId,
            Long currentUserId);

    // 일정 등록 알림 전송 (매니저 <-> 크리에이터)
    void sendScheduleRegistrationNotification(String senderName, String scheduleName, Long receiverId);

    // 휴가 신청 알림 (직원 -> 관리자)
    void sendVacationRequestNotification(String employeeName, String vacationType);

    // 휴가 처리 결과 알림 (관리자 -> 직원)
    void sendVacationResultNotification(Long employeeId, String vacationType, String result);

    // 알림 목록 조회
    List<NotificationDto> getNotifications(Long memberId);

    // 알림 읽음 처리
    void markAsRead(Long notificationId);

    // 모든 알림 읽음 처리
    void markAllAsRead(Long memberId);
}