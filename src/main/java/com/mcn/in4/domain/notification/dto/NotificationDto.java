package com.mcn.in4.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 알림 DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    // 알림 유형
    // - LEGAL_TAX_REGISTERED: 법률/세무 등록 알림
    private String type;

    // 알림 제목
    private String title;

    // 알림 메시지
    private String message;

    // 추가 데이터 (법률/세무 정보 등)
    private Object data;

    // 알림 생성 시각
    private LocalDateTime timestamp;

    // 읽음 여부
    private boolean isRead;
}