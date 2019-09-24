package com.example.rpl.RPL.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(value = "/api/health", produces = "plain/text")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }
}
