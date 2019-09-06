package com.etl.pro.Extractor.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {
	
	private static final Logger logger = LoggerFactory.getLogger(Producer.class);

	@Autowired
	JmsTemplate jmsTemplate;

	public void sendMessage(final String queueName, final String msg) {
		logger.debug("Sending this Message to Queue "+msg);
		jmsTemplate.convertAndSend(queueName, msg);
		
	}

}