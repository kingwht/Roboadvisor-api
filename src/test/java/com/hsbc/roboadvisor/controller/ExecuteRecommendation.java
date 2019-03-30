package com.hsbc.roboadvisor.controller;

import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Portfolio.Holding;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.model.Recommendation.Recommendation;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;
import com.hsbc.roboadvisor.model.Recommendation.TransactionType;
import com.hsbc.roboadvisor.payload.TransactionResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class ExecuteRecommendation extends PortfolioPreferenceControllerTest {
    private PortfolioPreference portfolioPreference = new PortfolioPreference();
    private Portfolio portfolio = new Portfolio();
    private Fund fund = new Fund();
    private Recommendation recommendation = new Recommendation();
    private TransactionResponse transactionResponse = new TransactionResponse();

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
        portfolio.setId("1");
        portfolio.setCustomerId("abc");
        recommendation.setRecommendationId(1);

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/execute"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
            //fail("Request should have failed");
        } catch (Exception e) {
            if(e.getClass() != MissingRequestHeaderException.class)
                fail("Received unexpected exception: " + e.getMessage());
        }
    }


    @Test
    public void throwsIfRecommendationIdIsNull() {
        portfolio.setId("1");
        portfolio.setCustomerId("abc");
        recommendation.setRecommendationId(null);
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/execute")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            fail("Received unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void throwsIfPortfolioIdDoesNotExist(){
        portfolio.setId("1");
        portfolio.setCustomerId("abc");
        recommendation.setPortfolioId("1");
        portfolioPreference.setPortfolioId("1");

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(portfolioPreference);
        when(fundSystemRequestService.getPortfolios(portfolio.getCustomerId())).thenReturn(Collections.emptyList());
        when(recommendationRepositoryService.findRecommendationByRecommendationId(recommendation.getRecommendationId()))
                .thenReturn(recommendation);

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/execute")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("No Exception should be thrown");
        }
    }

    @Test//(expectedExceptions = ResourceNotFoundException.class)
    public void throwsIfPortfolioPreferenceDoesNotExist() {
        portfolio.setId("1");
        portfolio.setCustomerId("abc");

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(null);

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/execute")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("No Exception should be thrown");
        }
    }

    @Test
    public void throwsIfRecommendationDoesNotExist() {
        portfolio.setId("1");
        portfolio.setCustomerId("abc");
        recommendation.setPortfolioId("1");
        portfolioPreference.setPortfolioId("1");

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(portfolioPreference);
        when(recommendationRepositoryService.findRecommendationByRecommendationId(recommendation.getRecommendationId()))
                .thenReturn(null);

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/execute")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("No Exception should be thrown");
        }
    }

    @Test
    public void normalRequest() {
        portfolio.setId("1");
        portfolio.setCustomerId("abc");
        fund.setFundId(1);
        fund.setFundName("stock");
        Holding holding = new Holding();
        holding.setFundId(1);
        holding.setUnits(50);
        portfolio.setHoldings(new ArrayList<Holding>(){{add(holding);}});
        recommendation.setPortfolioId("1");
        portfolioPreference.setPortfolioId("1");
        Transaction transaction = new Transaction();
        transaction.setAction(TransactionType.buy);
        transaction.setUnits(100);
        transaction.setFundId(1);
        List<Transaction> transactions = new ArrayList<Transaction>(){{add(transaction);}};
        recommendation.setTransactions(transactions);


        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(portfolioPreference);
        when(fundSystemRequestService.getPortfolios(portfolio.getCustomerId())).thenReturn(new ArrayList<Portfolio>(){{add(portfolio);}});
        when(recommendationRepositoryService.findRecommendationByRecommendationId(recommendation.getRecommendationId())).thenReturn(recommendation);


        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/execute")
                    .header("x-custid", portfolio.getCustomerId()))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            fail("No Exception should be thrown: " + e.getMessage());
        }
    }
}
