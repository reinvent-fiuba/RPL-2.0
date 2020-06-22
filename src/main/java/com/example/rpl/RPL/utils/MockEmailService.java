package com.example.rpl.RPL.utils;

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
}
