package com.mcn.in4.domain.notification.service;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.notification.dto.NotificationDto;
import com.mcn.in4.global.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 알림 서비스
// 실시간 알림 전송을 담당합니다.
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitters sseEmitters;
    private final MemberRepository memberRepository;

    // 법률/세무 등록 알림을 관리자에게 전송합니다.
    public void sendLegalTaxRegistrationNotification(String managerName, String type, Object data) {
        // ADMINISTRATOR 권한을 가진 모든 사용자 조회
        List<Member> admins = memberRepository.findAllByMemberRole(MemberRole.ADMINISTRATOR);

        if (admins.isEmpty()) {
            return;
        }

        // 알림 생성
        NotificationDto notification = NotificationDto.builder()
                .type("LEGAL_TAX_REGISTERED")
                .title("새로운 " + type + " 등록")
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

    // 법률/세무 승인(완료) 알림을 담당 매니저에게 전송합니다.
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
}