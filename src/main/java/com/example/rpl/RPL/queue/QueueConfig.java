package com.example.rpl.RPL.queue;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("hello-world")
@Configuration
public class QueueConfig {

    private RabbitTemplate template;

    @Autowired
    public QueueConfig(RabbitTemplate template) {
        this.template = template;
    }


    @Bean
    public Queue hello() {
        return new Queue("hello");
    }


    @Profile("consumer")
    @Bean
    public Consumer consumer() {
        return new Consumer();
    }


    @Profile("producer")
    @Bean
    public Producer producer() {
        return new Producer(template, hello());
    }
}
