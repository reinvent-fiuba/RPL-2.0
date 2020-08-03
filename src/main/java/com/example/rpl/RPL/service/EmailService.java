package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.Course;
import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
public class EmailService implements IEmailService {

    private String frontEndUrl;  // See BeanConfiguration.java

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender emailSender, TemplateEngine templateEngine,
        String frontEndUrl) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.frontEndUrl = frontEndUrl;
    }

    @Override
    public void sendValidateEmailMessage(String email, String token) {
        String link = frontEndUrl + "/user/validateEmail?token=" + token;
        MimeMessagePreparator message = prepareEmail(email, "RPL: Validar Email",
            this.buildTokenEmail(link, "validateEmailEmail"));

        emailSender.send(message);
    }

    @Override
    public void sendResetPasswordMessage(String email, String token) {
        String link = frontEndUrl + "/user/changePassword?token=" + token;
        MimeMessagePreparator message = prepareEmail(email, "RPL: Reseteo de contraseña",
            this.buildTokenEmail(link, "resetPasswordEmail"));

        emailSender.send(message);
    }

    @Override
    public void sendAcceptedStudentMessage(String email, CourseUser courseUser) {
        String link = frontEndUrl + "/courses/" + courseUser.getCourse().getId();

        MimeMessagePreparator message = prepareEmail(email, "RPL: Fuiste aceptado en " + courseUser.getCourse().getName(),
                this.buildAcceptedEmail(courseUser.getUser(), courseUser.getCourse(), link));

        emailSender.send(message);
    }

    private String buildTokenEmail(String link, String template) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process(template, context);
    }

    private String buildAcceptedEmail(User user, Course course, String link) {
        Context context = new Context();
        context.setVariable("name", user.getName());
        context.setVariable("surname", user.getSurname());
        context.setVariable("courseName", course.getName());
        context.setVariable("universityCourseId", course.getUniversityCourseId());
        context.setVariable("description", course.getDescription());
        context.setVariable("link", link);
        return templateEngine.process("acceptedToCourseEmail", context);
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
