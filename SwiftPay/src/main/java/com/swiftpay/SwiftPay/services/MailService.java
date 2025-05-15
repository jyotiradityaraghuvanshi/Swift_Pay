package com.swiftpay.SwiftPay.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String toEmail , String subject , String body , Map<String , Object> variables){
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("wargodsubrahmanya1108@gmail.com");
//        message.setTo(toEmail);
//        message.setSubject(subject);
//        message.setText(body);
//
//        mailSender.send(message);

        // Using mime message and thymeleaf context we are setting the template for the email body
        MimeMessage message = mailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message , true);
            helper.setFrom("wargodsubrahmanya1108@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(body , context);

            helper.setText(htmlContent,true);

            mailSender.send(message);
        }
        catch (MessagingException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

}
