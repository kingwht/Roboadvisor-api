package com.hsbc.roboadvisor.controller;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.model.Recommendation.Recommendation;

public class CreateRecommendationTest extends PortfolioPreferenceControllerTest {



    private PortfolioPreference portfolioPreference = new PortfolioPreference();
    private Portfolio portfolio = new Portfolio();
    private Fund fund = new Fund();
    private Recommendation recommendation = new Recommendation();

    private List<Portfolio> customerPortfolioList = new ArrayList<Portfolio>(){
        {add(portfolio);}};
    private List<Fund> customerFundList = new ArrayList<Fund>(){
        {add(fund);}};




    @BeforeClass
    public void setup() {
        super.setup();
    }

    @Test
    public void MissingHeader() {
        portfolio.setId(1);
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId() + "/rebalance"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            if(e.getClass() != MissingRequestHeaderException.class)
                fail("Received unexpected exception: " + e.getMessage());
        }
    }


    @Test
    public void throwsIfCustomerIdIsNull()  {
        portfolio.setId(1);
        portfolio.setCustomerId(null);
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId() + "/rebalance")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            if(e.getClass() != IllegalArgumentException.class)
                fail("Received unexpected exception: " + e.getMessage());
            else {
                System.out.println("Test fails as expected: " + e.getMessage());
            }
        }
    }

    @Test
    public void throwsIfPortfolioIdIsNull() {
        portfolio.setId(null);
        portfolio.setCustomerId("abc");
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId() + "/rebalance")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            fail("Received unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void throwsIfPortfolioIdDoesNotExist(){
        portfolio.setId(1);
        portfolio.setCustomerId("abc");
        portfolioPreference.setPortfolioId(1);

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(portfolioPreference);
        when(fundSystemRequestService.getPortfolios(portfolio.getCustomerId())).thenReturn(Collections.emptyList());
        when(fundSystemRequestService.getFunds(portfolio.getCustomerId())).thenReturn(Collections.emptyList());
        when(recommendationRepositoryService.findRecommendationByPortfolioId(portfolio.getId())).thenReturn(recommendation);
        when(recommendationRepositoryService.saveRecommendation(portfolio, Collections.emptyList(), portfolioPreference))
                .thenReturn(recommendation);

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId() + "/rebalance")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("Received unexpected exception: " + e.getMessage());
        }
    }

    @Test//(expectedExceptions = ResourceNotFoundException.class)
    public void throwsIfPortfolioPreferenceDoesNotExist() {
        portfolio.setId(1);
        portfolio.setCustomerId("abc");

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(null);

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId() + "/rebalance")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("Received unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void normalRequest() {
        portfolio.setId(1);
        portfolio.setCustomerId("abc");
        portfolioPreference.setPortfolioId(1);

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(portfolioPreference);
        when(fundSystemRequestService.getPortfolios(portfolio.getCustomerId())).thenReturn(customerPortfolioList);
        when(fundSystemRequestService.getFunds(portfolio.getCustomerId())).thenReturn(customerFundList);
        when(recommendationRepositoryService.findRecommendationByPortfolioId(portfolio.getId())).thenReturn(recommendation);
        when(recommendationRepositoryService.saveRecommendation(portfolio, customerFundList, portfolioPreference))
                .thenReturn(recommendation);

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/"+portfolio.getId()+"/rebalance")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.recommendationId", is(recommendation.getRecommendationId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.transactions", is(recommendation.getTransactions())));
        } catch (Exception e) {
            fail("Received unexpected exception: " + e.getMessage());
        }
    }

}
