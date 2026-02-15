package com.mcn.in4.domain.ai.service;

import com.mcn.in4.domain.ai.tools.AttendanceTools;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

/**
 * AI 챗봇 서비스 구현체
 * Spring AI의 ChatClient를 사용하여 OpenAI와 통신합니다.
 * 사용자 역할에 따라 사용 가능한 도구를 동적으로 등록합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

  public static final String MSG_NO_PERMISSION = "해당 기능에 대한 접근 권한이 없습니다.";

  private final ChatClient chatClient;

  /**
   * 역할별 사용 가능한 도구 매핑 (중앙 관리)
   * - 새 도구 추가 시: 해당 역할의 List에 도구 이름 추가
   * - 새 역할 추가 시: Map에 새 항목 추가
   * - CREATOR 등 맵에 없는 역할은 도구 없음 (빈 리스트 적용)
   */
  private static final Map<String, List<String>> ROLE_TOOLS = Map.of(
      "ADMINISTRATOR", List.of("getMyAttendanceSummary", "getAllAttendanceSummary",
          "getMyVacationSummary", "getAllVacationSummary", "searchMember"),
      "MANAGER", List.of("getMyAttendanceSummary", "getMyVacationSummary", "searchMember"),
      "EMPLOYEE", List.of("getMyAttendanceSummary", "getMyVacationSummary", "searchMember")
  // CREATOR → 맵에 없음 = 도구 없음
  );

  @Override
  @Transactional
  public String chat(String message) {
    log.info("AI Chat 요청: {}", message);

    try {
      // JWT 토큰에서 사용자 역할 추출
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String userRole = extractRole(auth);

      // 역할별 허용 도구 목록 조회
      List<String> allowedTools = ROLE_TOOLS.getOrDefault(userRole, List.of());
      log.info("AI Chat - 사용자 역할: {}, 허용 도구: {}", userRole, allowedTools);

      // 도구가 없는 역할(CREATOR 등)은 LLM 호출 없이 즉시 거부
      if (allowedTools.isEmpty()) {
        log.info("AI Chat - 도구 없음, 즉시 거부 반환");
        return MSG_NO_PERMISSION;
      }

      // 역할별 권한 지침
      String roleGuideline = allowedTools.contains("getAllAttendanceSummary")
          ? "- 특정 이름이 언급되면 반드시 'getAllAttendanceSummary'를 사용하세요 (getMyAttendanceSummary 사용 금지)."
          : "- 타인의 근태 조회 권한이 없습니다. 타인 이름이 언급되면 도구를 호출하지 말고 정확히 '" + MSG_NO_PERMISSION + "'이라고만 답하세요.";

      String systemMessage = """
          Current Date: %s

          당신은 주식회사 'CreatorFlex'의 인사 관리(HR) AI 비서입니다.
          한국어로 친절하고 전문적으로 답변하세요.

          [날짜 규칙]
          - 연도 미명시 시 2026년 기준
          - 구체적 날짜(N월 N일)는 startDate/endDate에 YYYY-MM-DD로 변환
          - 구체적 날짜(N월 N일)는 startDate/endDate에 YYYY-MM-DD로 변환
          - 상대적 표현(이번주, 저번달 등)은 dateInfo 필드 사용
          - 잔여 연차나 연간 내역 질문 시 dateInfo='THIS_YEAR' 사용

          [권한]
          %s

          [도구 사용 규칙]
          - 근태 조회: '나'의 근태는 getMyAttendanceSummary, 타인은 getAllAttendanceSummary 사용
          - 휴가 조회: '나'의 휴가/잔여 연차는 getMyVacationSummary, 타인은 getAllVacationSummary 사용
          - 직원 검색: 이름이나 부서로 직원 정보를 찾을 때 searchMember 사용

          [응답 규칙]
          - 도구 결과를 마크다운 표로 정리 (표 앞에는 반드시 빈 줄 추가)
          - 도구 호출 없이 근태/휴가 데이터를 추측하거나 만들어내지 마세요
          - 권한 없음, 오류 등 거부 응답은 한 문장으로 간결하게 (대안 제시나 절차 설명 금지)
          """.formatted(LocalDate.now(), roleGuideline);

      // 역할별 도구 동적 등록 후 LLM 호출
      ChatClient.ChatClientRequestSpec prompt = chatClient.prompt()
          .system(systemMessage)
          .user(message);

      if (!allowedTools.isEmpty()) {
        prompt.functions(allowedTools.toArray(String[]::new));
      }

      String reply;
      try {
        reply = prompt.call().content();
      } finally {
        // ThreadLocal 정리 (메모리 누수 방지)
        String deniedMessage = AttendanceTools.DENIED_MESSAGE.get();
        AttendanceTools.DENIED_MESSAGE.remove();

        // 도구에서 권한 거부 플래그가 설정되었으면 LLM 응답을 무시하고 거부 메시지 직접 반환
        if (deniedMessage != null) {
          log.info("AI Chat - 도구 권한 거부 감지, LLM 응답 무시. 거부 메시지: {}", deniedMessage);
          return deniedMessage;
        }
      }

      log.info("AI Chat 응답: {}", reply);
      return reply;
    } catch (Exception e) {
      log.error("AI Chat 에러: {}", e.getMessage(), e);

      // 연결 오류 판별
      if (isConnectionError(e)) {
        throw new CustomException(ErrorCode.AI_CONNECTION_FAILED,
            "AI 서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요. (" + e.getMessage() + ")");
      }

      // 기타 AI 오류
      // 사용자에게는 친절한 메시지 반환, 상세 로그는 서버에 기록됨
      throw new CustomException(ErrorCode.AI_SERVICE_ERROR,
          "AI 서비스 처리 중 일시적인 오류가 발생했습니다. 관리자에게 문의하거나 잠시 후 다시 시도해주세요.");
    }
  }

  /**
   * Authentication에서 사용자 역할을 추출합니다.
   */
  private String extractRole(Authentication auth) {
    if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
      return "UNKNOWN";
    }
    return auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(a -> a.startsWith("ROLE_"))
        .map(a -> a.substring(5))
        .findFirst()
        .orElse("UNKNOWN");
  }

  private boolean isConnectionError(Exception e) {
    String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
    return msg.contains("connection refused")
        || msg.contains("connect timed out")
        || msg.contains("host")
        || e.getCause() instanceof ConnectException;
  }
}
