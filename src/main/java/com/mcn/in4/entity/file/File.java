package com.mcn.in4.entity.file;

import com.mcn.in4.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "files") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;
    //파일 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    //업로더 키

    @Column(name = "s3_key", nullable = false)
    private String s3Key;
    //S3키

    @Column(name = "file_name", nullable = false)
    private String fileName;
    //파일명

    @Column(name = "content_type", nullable = false)
    private String contentType;
    //미디어타입

    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    //파일크기

    @Enumerated(EnumType.STRING)
    @Column(name = "file_status", nullable = false)
    private FileStatus fileStatus;
    //파일상태

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    //파일 등록 시간

    @Column(name = "completed_at", nullable = true)
    private LocalDateTime completedAt;
    //업로드 완료 시간
}
