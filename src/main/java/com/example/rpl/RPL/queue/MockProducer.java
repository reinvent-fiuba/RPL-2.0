package com.example.rpl.RPL.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MockProducer implements IProducer {

    @Autowired
    public MockProducer() {
    }

    @Override
    public void send(String submissionId, String language) {
        log.info(" [x] MOCK PRODUCER Sending '" + this.getMessage(submissionId, language) + "'");

        log.info(" [x] MOCK PRODUCER Sent '" + this.getMessage(submissionId, language) + "'");
    }

    private String getMessage(String submissionId, String language) {
        return submissionId + ' ' + language;
    }
}
