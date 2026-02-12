package com.mcn.in4.domain.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "건강검진 이미지 분석 결과")
public class ImageAnalysisResponse {

    @Schema(description = "검진일 (예: 2024-01-01)", example = "2024-03-15")
    private String examinationDate;

    @Schema(description = "검진 병원명", example = "서울대학교병원 강남센터")
    private String hospitalName;

    @Schema(description = "종합 판정 결과 및 소견", example = "정상A (특이소견 없음)")
    private String overallResult;
}