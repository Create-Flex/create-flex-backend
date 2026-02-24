package com.mcn.in4.domain.QnA.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "File")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QA_id", nullable = false)
    private QABoard qaBoard;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "s3key", nullable = false)
    private String s3Key;
}
