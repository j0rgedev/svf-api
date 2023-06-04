package com.integrador.svfapi.utils;

import com.integrador.svfapi.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class JMail {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    public void sendMail(
            String mailDestination,
            String title,
            String msgBody
    ){

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(mailDestination);
        email.setFrom(sender);
        email.setSubject(title);
        email.setText(msgBody);
        mailSender.send(email);
    }

}
