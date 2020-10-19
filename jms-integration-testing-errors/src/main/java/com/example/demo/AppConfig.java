package com.example.demo;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

@Configuration
public class AppConfig {

    @Bean
    public ActiveMQQueue jmsQueue() {
        ActiveMQQueue activeMQQueue = new  ActiveMQQueue("testQueue");
        return activeMQQueue;
    }

    @Bean
    public JmsQueueSender jmsQueueSender(JmsTemplate jmsTemplate, Queue queue) {
        return new JmsQueueSender(jmsTemplate, queue);
    }
}