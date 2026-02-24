package com.mcn.in4.domain.QnA.service;

import com.mcn.in4.domain.QnA.dto.FileResponseDto;
import com.mcn.in4.domain.QnA.entity.File;
import com.mcn.in4.domain.QnA.entity.QABoard;
import com.mcn.in4.domain.QnA.repository.FileRepository;
import com.mcn.in4.domain.QnA.repository.QARepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mcn.in4.domain.QnA.dto.QAResponseDto.*;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class QAServiceImpl implements QAService{
    private final QARepository qaRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;
    private final S3Presigner s3Presigner;
    private final MailService mailService;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Override
    public Page<QATitle> generateQATitleList(Long memberId, Long listPage){
        PageRequest pageRequest = PageRequest.of((listPage.intValue()-1), 10);

        Member member = memberRepository.findById(memberId).orElseThrow();
        Page<QABoard> qaBoards = (member.getMemberRole() == MemberRole.ADMINISTRATOR)
            ? qaRepository.findAllByOrderByQuestionTimeDesc(pageRequest)
                : qaRepository.findByQuestionMember_MemberIdOrderByQuestionTimeDesc(memberId, pageRequest);

        return qaBoards.map(qaBoard -> QATitle.builder()
                        .qaId(qaBoard.getQAId())
                        .questionTitle(qaBoard.getQuestionTitle())
                        .questionTime(qaBoard.getQuestionTime())
                        .questionMemberName(qaBoard.getQuestionMember().getMemberName())
                        .departmentName(qaBoard.getQuestionMember().getDepartment() != null
                                ? qaBoard.getQuestionMember().getDepartment().getDepartmentName()
                                : "부서없음")
                        .answered(qaBoard.getAnswerTime() != null)
                        .build()
                );
    }

    @Override
    public QADetail generateQADetail(Long qaId){
        QABoard qaBoard = qaRepository.findByQAId(qaId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문의입니다."));

        List<File> files = fileRepository.findByQaBoard_QAId(qaId);

        List<FileResponseDto> fileResponse =  new ArrayList<>();

        for(File file : files){

            String fileEncoded = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(file.getS3Key())
                    .responseContentDisposition("attachment; filename=\"" + fileEncoded + "\"")
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner
                    .presignGetObject(presignedRequest -> presignedRequest
                            .signatureDuration(Duration.ofMinutes(10))
                            .getObjectRequest(getObjectRequest));

            String downloadUrl = presignedGetObjectRequest.url().toString();

            fileResponse.add(new FileResponseDto(file.getFileName(), downloadUrl, file.getFileSize()));
        }

        return QADetail.builder()
                .qaId(qaBoard.getQAId())
                .questionTitle(qaBoard.getQuestionTitle())
                .questionTime(qaBoard.getQuestionTime())
                .questionMemberName(qaBoard.getQuestionMember().getMemberName())
                .questionDepartmentName(qaBoard.getQuestionMember().getDepartment() != null
                        ? qaBoard.getQuestionMember().getDepartment().getDepartmentName()
                        : "부서없음")
                .questionDetail(qaBoard.getQuestionDetail())
                .files(fileResponse)
                .answered(qaBoard.getAnswerTime() != null)
                .answerTime(qaBoard.getAnswerTime())
                .answerMemberName(qaBoard.getAnswerMember() != null
                        ? qaBoard.getAnswerMember().getMemberName()
                        : null)
                .answerDepartmentName(qaBoard.getAnswerMember() != null
                        ? qaBoard.getAnswerMember().getDepartment().getDepartmentName()
                        :null)
                .answerDetail(qaBoard.getAnswerDetail())
                .build();
    }

    @Override
    public QAFileUpload uploadQuestion(Long memberId, String questionTitle, String questionDetail, List<MultipartFile> files){
        Member questionMember = memberRepository.findById(memberId).orElseThrow();

        List<String> list = new ArrayList<>();

        QABoard qaBoard = QABoard.builder()
                .questionMember(questionMember)
                .questionTitle(questionTitle)
                .questionDetail(questionDetail)
                .questionTime(LocalDateTime.now())
                .build();

        QABoard board = qaRepository.save(qaBoard);

        mailService.sendUpQuest(questionTitle, questionMember.getMemberName(), questionMember.getDepartment().getDepartmentName(), questionDetail);

        if(files != null && !files.isEmpty()) {
            files.stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .forEach(file -> {
                        String fileName = file.getOriginalFilename();
                        String s3key = "public/file/" + UUID.randomUUID().toString() + "/" + fileName;
                        Long fileSize = file.getSize();
                        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(s3key)
                                .contentType(file.getContentType())
                                .build();

                        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                                PutObjectPresignRequest.builder()
                                        .putObjectRequest(putObjectRequest)
                                        .signatureDuration(Duration.ofHours(1))
                                        .build());

                        File newFile = File.builder()
                                .qaBoard(board)
                                .fileName(fileName)
                                .s3Key(s3key)
                                .fileSize(fileSize)
                                .build();
                        fileRepository.save(newFile);
                        list.add(presignedRequest.url().toString());
                    });
        }

        return QAFileUpload.builder()
                .uploadURL(list)
                .build();
    }

    @Override
    public void uploadAnswer(Long memberId, Long qaId, String answerDetail){
        Member answerMember = memberRepository.findById(memberId).orElseThrow();
        QABoard qaBoard = qaRepository.findByQAId(qaId).orElseThrow();
        MemberEmployeeDetail questionMemberDetail = memberEmployeeDetailRepository.findByMemberMemberId(qaBoard.getQuestionMember().getMemberId()).orElseThrow();

        mailService.sendUpAnswer(questionMemberDetail.getPersonalEmail(), qaBoard.getQuestionTitle(),
                                answerMember.getMemberName(), answerMember.getDepartment().getDepartmentName(),
                                qaBoard.getQuestionDetail(), answerDetail);

        qaBoard.setAnswerMember(answerMember);
        qaBoard.setAnswerDetail(answerDetail);
        qaBoard.setAnswerTime(LocalDateTime.now());
    }
}
