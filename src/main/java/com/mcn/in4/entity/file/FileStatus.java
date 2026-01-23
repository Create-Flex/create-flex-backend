package com.mcn.in4.entity.file;

public enum FileStatus {
    UPLOADING,   // presigned URL 발급됨, 업로드 진행 중
    COMPLETED,   // S3 업로드 완료 + 검증 완료
    FAILED,      // 업로드 실패
    DELETED      // 논리 삭제 (선택)
}
