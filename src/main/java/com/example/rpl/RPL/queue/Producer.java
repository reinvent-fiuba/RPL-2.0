package com.example.rpl.RPL.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class Producer {

    private RabbitTemplate template;

    private Queue queue;

    @Autowired
    public Producer(RabbitTemplate template, Queue queue) {
        this.template = template;
        this.queue = queue;
    }


    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send(String message) {
        this.template.convertAndSend(queue.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
