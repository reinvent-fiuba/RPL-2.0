package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.model.Ticket;
import com.example.rpl.RPL.queue.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@Profile("producer")
@RestController
public class HelloController {

    private final Producer producer;


    @Autowired
    public HelloController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/submit")
    public ResponseEntity<Ticket> submit(@Valid @RequestBody final Ticket ticket) {
        for (long i = 0; i < ticket.getQuantity(); i++) {
            String id = UUID.randomUUID().toString();
            producer.send(id);
        }
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }


    @RequestMapping(value="/health")
    public ResponseEntity health() {
        HttpStatus status;
        if (true) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }


}