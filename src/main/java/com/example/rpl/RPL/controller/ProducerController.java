package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.model.Ticket;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.queue.Producer;
import com.example.rpl.RPL.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@Profile("producer")
@RestController
public class ProducerController {

    private final Producer producer;

    private final UserRepository userRepository;


    @Autowired
    public ProducerController(Producer producer, UserRepository userRepository) {
        this.producer = producer;
        this.userRepository = userRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<Ticket> submit(@Valid @RequestBody final Ticket ticket) {
        User user = new User("Ale", "levinasale@gmail.com");
        userRepository.save(user);

        for (long i = 0; i < ticket.getQuantity(); i++) {
            String id = UUID.randomUUID().toString();
            producer.send(id);
        }
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }
}
