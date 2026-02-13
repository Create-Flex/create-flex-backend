package com.mcn.in4.domain.image.controller;

import com.mcn.in4.domain.image.dto.ImageAnalysisResponse;
import com.mcn.in4.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image Analysis", description = "이미지 분석 API")
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "건강검진표 분석", description = "건강검진 결과 이미지를 업로드하여 AI 분석 결과를 반환합니다.")
    @PostMapping(value = "/analyze/health-checkup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageAnalysisResponse> analyzeHealthCheckup(
            @RequestPart("file") MultipartFile file) {

        ImageAnalysisResponse response = imageService.analyzeHealthCheckupImage(file);
        return ResponseEntity.ok(response);
    }
}