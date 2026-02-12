package com.mcn.in4.domain.image.service;

import com.mcn.in4.domain.image.dto.ImageAnalysisResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageAnalysisResponse analyzeHealthCheckupImage(MultipartFile file);
}