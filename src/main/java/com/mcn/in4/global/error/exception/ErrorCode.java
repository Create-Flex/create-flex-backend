package com.mcn.in4.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 1. 공통 (Common)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "대상 엔티티를 찾을 수 없습니다."),
    // 2. 인증/인가 (Auth)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "로그인 정보가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    SAME_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다."),
    // 3. 회원/직원 (Member / Employee)
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    MEMBER_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "직원 상세 정보가 존재하지 않습니다."),
    DUPLICATE_MEMBER_ACCOUNT(HttpStatus.CONFLICT, "이미 존재하는 사번/아이디입니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필 정보를 찾을 수 없습니다."),
    // 4. 부서/팀 (Department / Team)
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "부서를 찾을 수 없습니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    UNAUTHORIZED_TEAM_ACCESS(HttpStatus.FORBIDDEN, "해당 팀에 대한 접근 권한이 없습니다."),
    // 5. 일정 (Schedule)
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    UNAUTHORIZED_SCHEDULE_ACCESS(HttpStatus.FORBIDDEN, "해당 일정에 대한 접근/수정 권한이 없습니다."),
    DISALLOW_COMPANY_SCHEDULE(HttpStatus.FORBIDDEN, "회사 일정을 등록할 권한이 없습니다."),
    // 6. 휴가 (Vacation)
    VACATION_NOT_FOUND(HttpStatus.NOT_FOUND, "휴가 정보를 찾을 수 없습니다."),
    INSUFFICIENT_VACATION_REMAINDER(HttpStatus.BAD_REQUEST, "잔여 연차 일수가 부족합니다."),
    INVALID_VACATION_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 휴가 타입입니다."),
    VACATION_DETAIL_REQUIRED(HttpStatus.BAD_REQUEST, "휴가 타입별 필수 상세 정보가 누락되었습니다."),
    INVALID_VACATION_STATUS(HttpStatus.BAD_REQUEST, "현재 상태에서는 처리할 수 없는 요청입니다."),
    VACATION_DATE_OVERLAP(HttpStatus.CONFLICT, "이미 신청한 휴가와 날짜가 겹칩니다."),
    // 7. 근태 (Attendance)
    ATTENDANCE_ALREADY_MARKED(HttpStatus.CONFLICT, "이미 오늘 출근 처리가 되었습니다."),
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "출근 기록을 찾을 수 없습니다."),
    INVALID_ATTENDANCE_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 근태 상태입니다."),
    ALREADY_CHECKED_OUT(HttpStatus.BAD_REQUEST, "이미 퇴근 처리가 완료되었습니다."),
    // 8. 크리에이터/광고/계약 (Creator / Ad / Contract)
    CREATOR_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 크리에이터입니다."),
    CREATOR_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "크리에이터 상세정보가 존재하지 않습니다."),
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매니저입니다."),
    ADVERTISEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "광고 캠페인을 찾을 수 없습니다."),
    INVALID_ADVERTISEMENT_STATUS(HttpStatus.BAD_REQUEST, "대기중 상태의 광고만 처리할 수 있습니다."),
    CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND, "계약 정보를 찾을 수 없습니다."),
    INVALID_CONTRACT_PERIOD(HttpStatus.BAD_REQUEST, "계약 시작일은 종료일보다 이전이어야 합니다."),
    CONTRACT_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "계약서 파일이 존재하지 않습니다."),
    LEGALTAX_NOT_FOUND(HttpStatus.NOT_FOUND, "법률/세무 정보를 찾을 수 없습니다."),

    // 10. 크리에이터 Todo (CreatorTodo)
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "할 일을 찾을 수 없습니다."),

    // 11. AI 챗봇 (AI)
    AI_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI 응답 생성 중 오류가 발생했습니다."),
    AI_CONNECTION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "AI 서버에 연결할 수 없습니다."),


    // 11. 채팅방
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다.");
    private final HttpStatus status;
    private final String message;
}