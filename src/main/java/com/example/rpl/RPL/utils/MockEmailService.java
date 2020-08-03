package com.example.rpl.RPL.utils;

import com.example.rpl.RPL.model.CourseUser;
import com.example.rpl.RPL.service.IEmailService;

public class MockEmailService implements IEmailService {

    @Override
    public void sendValidateEmailMessage(String email, String token) {
        System.out.println("sending validation email to " + email + "with token " + token);
    }

    @Override
    public void sendResetPasswordMessage(String email, String token) {
        System.out.println("resending password email to " + email + "with token " + token);
    }

    public void sendAcceptedStudentMessage(String email, CourseUser courseUser) {
        System.out.println("sending acceptance email to " + email + "in course" + courseUser.getCourse().getName());
    }
}
