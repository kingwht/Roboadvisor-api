package com.hsbc.roboadvisor.controller;

import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.hsbc.roboadvisor.model.PortfolioPreference.Allocation;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioType;
import com.hsbc.roboadvisor.service.FundRequestService;
import com.hsbc.roboadvisor.service.PortfolioRepositoryService;
import com.hsbc.roboadvisor.service.RecommendationRepositoryService;

public class PortfolioPreferenceControllerTest {

	@InjectMocks
	private PortfolioController             portfolioController;

	@Mock
	private PortfolioRepositoryService      portfolioRepositoryService;
	private RecommendationRepositoryService recommendationRepositoryService;
	private FundRequestService              fundRequestService;

	private MockMvc mockMvc;

	@BeforeClass
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.portfolioController = new PortfolioController(portfolioRepositoryService,
				recommendationRepositoryService, fundRequestService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.portfolioController).build();
	}

	@Test
	public void testGetPortfolioPreference()
			throws Exception
	{
		List<Allocation> allocationsList = new ArrayList<>();
		Allocation allocation = new Allocation();
		allocation.setCategory(3);
		allocation.setFundId(54321);
		allocation.setPercentage(new BigDecimal(100));
		allocationsList.add(allocation);

		PortfolioPreference portfolioPreference = new PortfolioPreference();
		portfolioPreference.setDeviation(5);
		portfolioPreference.setPortfolioId(12345);
		portfolioPreference.setPortfolioType(PortfolioType.fund);
		portfolioPreference.setAllocations(allocationsList);

		Integer portfolioId = 12345;

		Mockito.when(this.portfolioRepositoryService.findPreferenceByPortfolioId(portfolioId)).thenReturn(portfolioPreference);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/roboadvisor/portfolio/" + portfolioId)
						.header("x-custid", "customer_id"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.deviation", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolioId", is(12345)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolioType", is("fund")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.allocations").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.allocations[0].fundId", is(54321)));
	}

	

}


