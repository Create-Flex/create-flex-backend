package com.mcn.in4.domain.QnA.service;

import com.mcn.in4.domain.department.entity.Department;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${mail.username}")
    private String username;

    private final JavaMailSender mailSender;

    public void sendUpQuest(String title, String memberName, String departmentName, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(username);
            helper.setSubject("[문의] : " + title);
            helper.setText("<h2>[문의자:" + memberName + " / 문의부서:"
                            + departmentName + "]</h2>" + "<p>"
                            + content + "</p>", true);
            helper.setFrom(username);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패", e);
        }
    }

    public void sendUpAnswer(String toMail, String title,
                             String memberName, String departmentName,
                             String quest, String answer) {

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toMail);
            helper.setSubject("[답변] : " + title);
            helper.setText("<i><h3>[문의사항] " + quest + "</h3></i>" + "<p></p>" +
                    "<h2>[답변자:" + memberName + " / 답변부서:" + departmentName + "]</h2>"
                    + "<p>" + answer + "</p>", true);
            helper.setFrom(username);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패", e);
        }
    }
}
