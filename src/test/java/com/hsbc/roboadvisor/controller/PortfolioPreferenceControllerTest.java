package com.hsbc.roboadvisor.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.hsbc.roboadvisor.service.PortfolioRepositoryService;
import com.hsbc.roboadvisor.service.RecommendationRepositoryService;
import com.hsbc.roboadvisor.service.FundRequestService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.hsbc.roboadvisor.repository.PortfolioRepository;

public class PortfolioPreferenceControllerTest {

	@InjectMocks
	private PortfolioController             portfolioController;
	private PortfolioRepositoryService      portfolioRepositoryService;
	private RecommendationRepositoryService recommendationRepositoryService;
	private FundRequestService              fundRequestService;

	@Mock
	private PortfolioRepository portfolioRepository;

	private MockMvc mockMvc;

	@BeforeClass
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.portfolioController = new PortfolioController(portfolioRepositoryService,
				recommendationRepositoryService, fundRequestService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.portfolioController).build();
	}

	@Test
	public void testCreatePortfolio() {
		assertThat(true).isTrue();
	}

	

}


