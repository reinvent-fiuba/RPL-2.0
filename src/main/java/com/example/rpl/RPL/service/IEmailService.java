package com.example.rpl.RPL.service;

public interface IEmailService {

    void sendValidateEmailMessage(String email, String token);

    void sendResetPasswordMessage(String email, String token);
}
