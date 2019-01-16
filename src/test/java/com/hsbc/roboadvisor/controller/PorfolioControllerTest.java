package com.hsbc.roboadvisor.controller;

import com.hsbc.roboadvisor.controller.PortfolioController;
import com.hsbc.roboadvisor.repository.PortfolioRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PorfolioControllerTest {

	@InjectMocks
	private PortfolioController portfolioController;

	@Mock
	private PortfolioRepository portfolioRepository;

	private MockMvc mockMvc;

	@Test
	public void testCreatePortfolio() {
		assertThat(portfolioController).isNotNull();
	}

	

}


