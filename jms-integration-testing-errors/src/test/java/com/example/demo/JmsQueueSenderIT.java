package com.example.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerRegistry;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(AppConfig.class)
public class JmsQueueSenderIT {

	private static final String MESSAGE = "hello queue world";

	@Qualifier("jmsQueueSender")
	@Autowired
	JmsQueueSender sender;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ActiveMQQueue jmsQueue;

	@Test
	public void simpleSend() {
		sender.simpleSend();

		String message = (String) jmsTemplate.receiveAndConvert(jmsQueue);
		assertEquals(MESSAGE, message, "Wrong message");
	}

	@Test
	public void simpleSend_withDetails() {
		sender.simpleSend();

		BrokerRegistry brokerRegistry = BrokerRegistry.getInstance();
		if (brokerRegistry.lookup("localhost") == null) {
			System.out.println("123");
		}

		String message = (String) jmsTemplate.receiveAndConvert(jmsQueue);
		assertEquals(MESSAGE, message, "Wrong message");
	}

	/**
	 * JSM ConnectionFactory correlated (via @DependsOn) with Embedded Broker test configuration.
	 */
	@Configuration
	protected static class TestConfiguration {

		@Bean
		public BrokerService brokerService() throws Exception {
			BrokerService broker = new BrokerService();
			broker.setPersistent(false);
			broker.addConnector("vm://localhost");
			return broker;
		}

		@Bean
		@DependsOn("brokerService")
		public ActiveMQConnectionFactory connectionFactory() {
			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
			activeMQConnectionFactory.setBrokerURL("vm://localhost?create=false");
			return activeMQConnectionFactory;
		}

		@Bean
		public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
			final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
			return jmsTemplate;
		}
	}
}