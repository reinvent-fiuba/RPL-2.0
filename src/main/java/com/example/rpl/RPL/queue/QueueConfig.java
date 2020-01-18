package com.example.rpl.RPL.queue;


import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class QueueConfig {

    private RabbitTemplate template;

    @Autowired
    public QueueConfig(RabbitTemplate template) {
        this.template = template;
    }


    @Bean
    public Queue hello() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 3600000);
        return new Queue("hello", true, false, false, args);
    }


    @Profile("consumer")
    @Bean
    public Consumer consumer() {
        return new Consumer();
    }


    @Profile("producer")
    @Bean
    public IProducer producer() {
        return new Producer(template, hello());
    }


    @Profile({"test-functional", "test-unit"})
    @Bean
    public IProducer mockProducer() {
        return new MockProducer();
    }
}
