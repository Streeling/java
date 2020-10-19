package com.example.demo;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * Inspired from https://docs.spring.io/spring-framework/docs/5.2.x/spring-framework-reference/integration.html#jms
 */
public class JmsQueueSender {
	private JmsTemplate jmsTemplate;
	private Queue queue;

	public JmsQueueSender(JmsTemplate jmsTemplate, Queue queue) {
		this.jmsTemplate = jmsTemplate;
		this.queue = queue;
	}

//	public void setConnectionFactory(ConnectionFactory cf) {
//		this.jmsTemplate = new JmsTemplate(cf);
//	}
//
	public void simpleSend() {
		this.jmsTemplate.send(this.queue, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("hello queue world");
			}
		});
	}
}
