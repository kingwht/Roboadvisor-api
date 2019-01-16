package com.hsbc.roboadvisor;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackageClasses = { RoboadvisorApplication.class})
public class RoboadvisorApplication {
	
	private static final Logger _logger = LoggerFactory.getLogger(RoboadvisorApplication.class);

	@PostConstruct
	void init() {
		_logger.info("RoboAdvisor application init.");
	}
	public static void main(String[] args) {
		SpringApplication.run(RoboadvisorApplication.class, args);
	}

}

