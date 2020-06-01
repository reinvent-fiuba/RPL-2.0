package com.example.rpl.RPL.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
public class EmailService {

    private String frontEndUrl;  // See BeanConfiguration.java

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender emailSender, TemplateEngine templateEngine,
        String frontEndUrl) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.frontEndUrl = frontEndUrl;
    }

    void sendValidateEmailMessage(String email, String token) {
        String link = frontEndUrl + "/user/validateEmail?token=" + token;
        MimeMessagePreparator message = prepareEmail(email, "RPL: Validar Email",
            this.buildTokenEmail(link, "validateEmailEmail"));

        emailSender.send(message);
    }

    void sendResetPasswordMessage(String email, String token) {
        String link = frontEndUrl + "/user/changePassword?token=" + token;
        MimeMessagePreparator message = prepareEmail(email, "RPL: Reseteo de contraseña",
            this.buildTokenEmail(link, "resetPasswordEmail"));

        emailSender.send(message);
    }

    private String buildTokenEmail(String link, String template) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process(template, context);
    }

    private MimeMessagePreparator prepareEmail(String recipient, String subject, String message) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, true);
        };
    }
}
