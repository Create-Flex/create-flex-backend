package com.mcn.in4.domain.QnA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class FileResponseDto {
    String fileName;
    String fileURL;
    Long fileSize;

    public FileResponseDto(String fileName, String fileURL, Long fileSize) {
        this.fileName = fileName;
        this.fileURL = fileURL;
        this.fileSize = fileSize;
    }
}
