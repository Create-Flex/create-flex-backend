package com.mcn.in4.domain.image.service;

import com.mcn.in4.domain.image.dto.ImageAnalysisResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.model.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ChatModel openAiChatModel;

    @Override
    public ImageAnalysisResponse analyzeHealthCheckupImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        try {
            // 결과 변환기 생성 (AI 응답 -> DTO 자동 매핑)
            BeanOutputConverter<ImageAnalysisResponse> converter =
                    new BeanOutputConverter<>(ImageAnalysisResponse.class);

            // 이미지 리소스 준비
            var imageResource = new ByteArrayResource(file.getBytes());

            // 프롬프트 작성 (JSON 포맷 요청 포함)
            // converter.getFormat()이 AI에게 JSON 스키마를 제공합니다.
            String promptText = """
                당신은 의료 데이터 추출 전문가입니다. 
                업로드된 건강검진 결과표 이미지를 분석하여 다음 정보를 추출해주세요:
                1. 검진일 (날짜 형식으로, 없으면 '알 수 없음')
                2. 검진병원명 (없으면 '알 수 없음')
                3. 종합 판정 결과 (종합 소견이나 판정 등급 요약)
                
                결과는 반드시 아래의 JSON 포맷을 따라야 합니다.
                {format}
                """;

            // {format} 부분을 실제 스키마 지시어로 치환
            String finalPrompt = promptText.replace("{format}", converter.getFormat());

            // 멀티모달 메시지 생성
            var userMessage = new UserMessage(
                    finalPrompt,
                    List.of(new Media(MimeTypeUtils.parseMimeType(file.getContentType()), imageResource))
            );

            // AI 호출 및 결과 파싱
            ChatResponse response = openAiChatModel.call(new Prompt(userMessage));
            String content = response.getResult().getOutput().getText();

            // JSON 문자열을 DTO로 변환하여 반환
            return converter.convert(content);

        } catch (Exception e) {
            log.error("Health checkup image analysis failed", e);
            throw new RuntimeException("건강검진표 분석 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}