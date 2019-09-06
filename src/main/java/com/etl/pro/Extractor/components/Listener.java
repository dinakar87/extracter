package com.etl.pro.Extractor.components;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.apache.activemq.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class Listener {
	
	@Value("${ftp.report.path}")
	private String repPath;
	
	private static final Logger logger = LoggerFactory.getLogger(Listener.class);
	
	BufferedWriter writer=null;
	@JmsListener(destination = "inbound.queue")
	public void receiveMessage(final Message jsonMessage) throws JMSException, IOException {
		
		logger.debug("Receiving this Message"+jsonMessage);
		
		String messageData = null;
		Map map =null;

		if(jsonMessage instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)jsonMessage;
			messageData = textMessage.getText();
			map = new Gson().fromJson(messageData, Map.class);
		}

		try {
			writer = new BufferedWriter(new FileWriter(repPath, true));
			writer.newLine();
			writer.write(map.get("client_id")+","+map.get("time_stamp"));
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			writer.close();
		}


	}

}