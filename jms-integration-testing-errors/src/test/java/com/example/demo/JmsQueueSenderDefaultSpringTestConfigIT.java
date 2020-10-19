package com.example.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerRegistry;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(AppConfig.class)
public class JmsQueueSenderDefaultSpringTestConfigIT {

	private static final String MESSAGE = "hello queue world";

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ActiveMQQueue jmsQueue;

	@Qualifier("jmsQueueSender")
	@Autowired
	JmsQueueSender sender;

	@Test
	public void simpleSend() {
		sender.simpleSend();

		String message = (String) jmsTemplate.receiveAndConvert(jmsQueue);
		assertEquals(MESSAGE, message, "Wrong message");
	}

	@Test
	public void simpleSend_withDetails() {
		sender.simpleSend();

		assertThat("Wrong default JmsTemplate ConnectionFactory", jmsTemplate.getConnectionFactory(), instanceOf(CachingConnectionFactory.class));
		assertThat("Wrong default JmsTemplate Target ConnectionFactory",
				((CachingConnectionFactory) jmsTemplate.getConnectionFactory()).getTargetConnectionFactory(),
				instanceOf(ActiveMQConnectionFactory.class));

		BrokerRegistry brokerRegistry = BrokerRegistry.getInstance();
		assertThat("Missing embedded broker", brokerRegistry.getInstance().lookup("localhost"), is(notNullValue()));

		String message = (String) jmsTemplate.receiveAndConvert(jmsQueue);

		assertThat("Wrong message received", MESSAGE, is(message));
	}
}