package com.bridgelabz.googlekeep.User.service;

import com.bridgelabz.googlekeep.utility.TokenUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String emailId,String subject,String text) {
            var mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailId);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            mailMessage.setFrom("shindekomal8080@gmail.com");
            javaMailSender.send(mailMessage);
    }

    public String getUrl(UUID id) {
        TokenUtil tokenUtil = new TokenUtil();
        return "http://localhost:8080/user/" + tokenUtil.createToken(id);
    }

    public String getPasswordResetUrl(UUID id) {
        TokenUtil tokenUtil = new TokenUtil();
        return "http://localhost:8080/user/passwordReset/" + tokenUtil.createToken(id);
    }
}
