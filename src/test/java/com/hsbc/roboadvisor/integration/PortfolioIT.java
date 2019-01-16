package com.hsbc.roboadvisor.integration;

import com.hsbc.roboadvisor.controller.PortfolioController;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PortfolioIT {

	@Autowired
	private PortfolioController portfolioController;

	@Test
	public void contextLoads() {
		assertThat(portfolioController).isNotNull();
	}

	

}


