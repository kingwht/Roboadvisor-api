package com.hsbc.roboadvisor.controller;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;

import com.hsbc.roboadvisor.service.FundRequestService;
import com.hsbc.roboadvisor.service.PortfolioRepositoryService;
import com.hsbc.roboadvisor.service.RecommendationRepositoryService;

public abstract class PortfolioPreferenceControllerTest {

	@InjectMocks
	protected PortfolioController             portfolioController;

	@Mock protected PortfolioRepositoryService      portfolioRepositoryService;
	@Mock protected RecommendationRepositoryService recommendationRepositoryService;
	@Mock protected FundRequestService              fundRequestService;
	protected MockMvc mockMvc;

	@BeforeClass
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.portfolioController = new PortfolioController(portfolioRepositoryService,
				recommendationRepositoryService, fundRequestService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.portfolioController).build();
	}
}


