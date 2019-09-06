package com.etl.pro.Extractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJms
public class ExtractorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExtractorApplication.class, args);
	}

}
