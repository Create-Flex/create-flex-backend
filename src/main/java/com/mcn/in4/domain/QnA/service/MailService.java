package com.mcn.in4.domain.QnA.service;

import com.mcn.in4.domain.department.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${mail.username}")
    private String username;

    private final JavaMailSender mailSender;

    public void sendUpQuest(String title, String memberName, String departmentName, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(username);
        message.setSubject("[문의] : " + title);
        message.setText("[문의자:"+ memberName + " / 문의부서:" + departmentName + "]\n" + content);
        message.setFrom(username);

        mailSender.send(message);
    }

    public void sendUpAnswer(String toMail, String title,
                             String memberName, String departmentName,
                             String quest, String answer) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toMail);
        message.setSubject("[답변] : " + title);
        message.setText("[문의사항]\n" + quest + "\n\n" +
                "[답변자:" + memberName + " / 답변부서:" + departmentName + "]\n"
                + answer );
        message.setFrom(username);

        mailSender.send(message);
    }
}
