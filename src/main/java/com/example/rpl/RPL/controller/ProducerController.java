package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.model.Ticket;
import com.example.rpl.RPL.queue.Producer;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Profile("producer")
@RestController
public class ProducerController {

    private final Producer producer;

    @Autowired
    public ProducerController(Producer producer) {
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
}