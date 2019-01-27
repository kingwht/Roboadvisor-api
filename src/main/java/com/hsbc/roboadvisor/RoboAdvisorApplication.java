package com.hsbc.roboadvisor;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EntityScan(basePackageClasses = { RoboAdvisorApplication.class})
public class RoboAdvisorApplication {
	
	private static final Logger _logger = LoggerFactory.getLogger(RoboAdvisorApplication.class);

	@PostConstruct
	void init() {
		_logger.info("RoboAdvisor application init.");
	}
	public static void main(String[] args) {
		SpringApplication.run(RoboAdvisorApplication.class, args);
	}

}


