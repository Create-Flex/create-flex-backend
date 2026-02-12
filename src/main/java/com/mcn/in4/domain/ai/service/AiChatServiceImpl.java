package com.mcn.in4.domain.ai.service;

import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.net.ConnectException;

/**
 * AI 챗봇 서비스 구현체
 * Spring AI의 ChatClient를 사용하여 Ollama와 통신합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

  private final ChatClient chatClient;

  @Override
  @Transactional
  public String chat(String message) {
    log.info("AI Chat 요청: {}", message);

    try {
      String systemMessage = """
          Current Date: %s

          당신은 주식회사 'CreatorFlex'의 인사 관리(HR) AI 비서입니다.
          사용자의 질문에 한국어로 친절하고 전문적으로 답변해야 합니다.

          [중요: 날짜 변환 규칙 - 반드시 준수]
          - 오늘은 위 'Current Date'에 명시된 날짜입니다.
          - 사용자가 연도를 명시하지 않으면 무조건 2026년을 기준으로 합니다.
          - 한국어 월(月)을 숫자로 정확히 변환하세요:
            1월=01, 2월=02, 3월=03, 4월=04, 5월=05, 6월=06,
            7월=07, 8월=08, 9월=09, 10월=10, 11월=11, 12월=12
          - 예시 (매우 중요):
            "1월 1일부터 1월 10일" → startDate="2026-01-01", endDate="2026-01-10"
            "2월 근태" → startDate="2026-02-01", endDate="2026-02-28"
            "3월 5일" → startDate="2026-03-05", endDate="2026-03-05"
          - 절대 사용자가 말한 월(月)을 다른 월로 변환하지 마세요.
            "1월"이라고 하면 반드시 01월(January)입니다. 현재 월과 혼동하지 마세요.

          [사용 가능한 도구]
          1. 'getMyAttendanceSummary' - 본인의 근태 기록 조회
             - "내 근태", "내 출퇴근", "오늘 출근했나?" 등 주어가 '나(Me)'일 때 사용

          2. 'getAllAttendanceSummary' - 전체 또는 특정 직원 근태 조회 (관리자 전용)
             - "전체 직원 근태", "회사 근태 현황" -> targetName=null
             - "**홍길동** 근태 보여줘", "**김철수** 출퇴근", "**이채용**의 2월 근태" -> targetName="홍길동", "김철수", "이채용"
             - 특정 이름이 언급되면 무조건 이 도구를 사용하고, targetName에 이름을 넣으세요.

          [지침]
          1. 날짜 포맷: 'YYYY-MM-DD' (예: 2026-01-15)
          2. '저번주', '이번달', '어제' 등 상대적 표현은 직접 계산하지 말고 도구의 'dateInfo' 필드를 사용하세요.
          3. 'N월 N일' 같은 구체적 날짜는 반드시 startDate/endDate에 YYYY-MM-DD로 변환하여 입력하세요.
          4. **중요**: 질문에 특정 이름("홍길동", "이채용" 등)이 있으면 절대 'getMyAttendanceSummary'를 쓰지 말고, 'getAllAttendanceSummary'를 쓰세요.
          5. 도구 결과를 마크다운 표(table)로 깔끔하게 정리하세요.
          """.formatted(LocalDate.now());

      String reply = chatClient.prompt()
          .system(systemMessage)
          .user(message)
          .call()
          .content();

      log.info("AI Chat 응답: {}", reply);
      return reply;
    } catch (Exception e) {
      log.error("AI Chat 에러: {}", e.getMessage(), e);

      // 연결 오류 판별
      if (isConnectionError(e)) {
        throw new CustomException(ErrorCode.AI_CONNECTION_FAILED,
            "Ollama 서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요. (" + e.getMessage() + ")");
      }

      // 기타 AI 오류
      throw new CustomException(ErrorCode.AI_SERVICE_ERROR,
          "AI 응답 생성 실패: " + e.getMessage());
    }
  }

  private boolean isConnectionError(Exception e) {
    String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
    return msg.contains("connection refused")
        || msg.contains("connect timed out")
        || msg.contains("host")
        || e.getCause() instanceof ConnectException;
  }
}
