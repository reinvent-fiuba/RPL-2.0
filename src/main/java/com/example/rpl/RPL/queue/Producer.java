package com.example.rpl.RPL.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class Producer implements IProducer {

    private final RabbitTemplate template;

    private final Queue queue;

    @Autowired
    public Producer(RabbitTemplate template, Queue queue) {
        this.template = template;
        this.queue = queue;
    }

    @Override
    public void send(String submissionId, String language) {
        log.info(" [x] Sending '" + this.getMessage(submissionId, language) + "'");
        this.template.convertAndSend(queue.getName(), this.getMessage(submissionId, language));
        log.info(" [x] Sent '" + this.getMessage(submissionId, language) + "'");
    }

    private String getMessage(String submissionId, String language) {
        return submissionId + ' ' + language;
    }
}
