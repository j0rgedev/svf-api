package com.integrador.svfapi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class JMail {

    @Autowired
    private JavaMailSender mailSender;
    public void sendMail(
            String mailDestination,
            String title,
            String msgBody
    ){

        SimpleMailMessage email = new SimpleMailMessage();

        //email.setTo("wolf3976@gmail.com");
        email.setTo(mailDestination);
        email.setFrom("U17206511@utp.edu.pe");
        //email.setSubject("Mensaje de Prueba 1");
        email.setSubject(title);
        //email.setText("No responder esto es una prueba");
        email.setText(msgBody);

        mailSender.send(email);
    }

}
