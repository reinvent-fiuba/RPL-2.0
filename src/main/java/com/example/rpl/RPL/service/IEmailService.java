package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.CourseUser;

public interface IEmailService {

    void sendValidateEmailMessage(String email, String token);

    void sendResetPasswordMessage(String email, String token);

    void sendAcceptedStudentMessage(String email, CourseUser courseUser);
}
