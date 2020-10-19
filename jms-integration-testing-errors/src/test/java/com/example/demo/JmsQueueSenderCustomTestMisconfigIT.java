package com.example.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerRegistry;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Import(AppConfig.class)
public class JmsQueueSenderCustomTestMisconfigIT {

	private static final String MESSAGE = "hello queue world";

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ActiveMQQueue jmsQueue;

	@Qualifier("jmsQueueSender")
	@Autowired
	private JmsQueueSender sender;

	private BrokerRegistry brokerRegistry;

	@BeforeAll
	public void setUpOnce() {
		brokerRegistry = BrokerRegistry.getInstance();
	}

	/**
	 * This test will fail with java.util.concurrent.TimeoutException: testSendAndReceive() timed out after 10 seconds
	 * because jmsTemplate.receiveAndConvert(Destination) will never exit.
	 */
	@Timeout(10L)
	@Test
	public void simpleSend_synchronousSendAndReceive() {
		sender.simpleSend();

		String message = (String) jmsTemplate.receiveAndConvert(jmsQueue);
		assertEquals(MESSAGE, message, "Wrong message");
	}

	/**
	 * This test will fail but at least you'll se why.
	 */
	@Timeout(10L)
	@Test
	public void simpleSend_synchronousSendAndReceive_withDetails_twoBrokersInsteadOfOne() {
		sender.simpleSend();

		BrokerRegistry brokerRegistry = BrokerRegistry.getInstance();
		assertNull(brokerRegistry.lookup("localhost"));
		assertEquals(0, brokerRegistry.getBrokers().size());
		// Since there are no running brokers this convertAndSend will start a new one which does not have the
		// previously sent message, thus this method call will never exit
		String message = (String) jmsTemplate.receiveAndConvert(jmsQueue);
		assertEquals(MESSAGE, message, "Wrong message");
	}

	/**
	 * Sometime this test will fail, when it does inspect the "Number of brokers:" console message.
	 */
	@Test
	public void simpleSend_asynchronousSendAndReceive() throws InterruptedException, ExecutionException, TimeoutException {
		Future<String> futureReceivedAndConvertedMessage = Executors.newSingleThreadExecutor().submit(
				() -> (String) jmsTemplate.receiveAndConvert(jmsQueue));

		System.out.println("Number of brokers: " + brokerRegistry.getBrokers().size());
		sender.simpleSend();

		String message = futureReceivedAndConvertedMessage.get(10, TimeUnit.SECONDS);
		assertThat("Wrong message received", MESSAGE, is(message));
	}

	/**
	 * This test will fail more often.
	 */
	@Test
	public void simpleSend_asynchronousSendAndReceive_withDetails_twoBrokers() throws InterruptedException, ExecutionException, TimeoutException {
		Future<String> futureReceivedAndConvertedMessage = Executors.newSingleThreadExecutor().submit(
				() -> {
					Thread.sleep(TimeUnit.SECONDS.toMillis(1));
					return (String) jmsTemplate.receiveAndConvert(jmsQueue);
				});

		sender.simpleSend();

		String message = futureReceivedAndConvertedMessage.get(10, TimeUnit.SECONDS);
		assertThat("Wrong message received", MESSAGE, is(message));
	}

	@Configuration
	protected static class TestConfiguration {

		@Bean
		public ActiveMQConnectionFactory connectionFactory() {
			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
			activeMQConnectionFactory.setBrokerURL("vm://localhost?broker.persistent=false");
			return activeMQConnectionFactory;
		}

		@Bean
		public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
			final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
			return jmsTemplate;
		}
	}
}