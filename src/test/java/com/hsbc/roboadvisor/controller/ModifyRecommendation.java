package com.hsbc.roboadvisor.controller;

import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Portfolio.Holding;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.model.Recommendation.Recommendation;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;
import com.hsbc.roboadvisor.model.Recommendation.TransactionType;
import com.hsbc.roboadvisor.payload.TransactionResponse;
import com.hsbc.roboadvisor.service.JpaJsonConverter;
import org.neo4j.unsafe.impl.batchimport.input.MissingHeaderException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ModifyRecommendation extends PortfolioPreferenceControllerTest {
    private JpaJsonConverter jpaJsonConverter = new JpaJsonConverter();
    private PortfolioPreference portfolioPreference = new PortfolioPreference();
    private Portfolio portfolio = new Portfolio();
    private Fund fund = new Fund();
    private Recommendation recommendation = new Recommendation();
    private TransactionResponse transactionResponse = new TransactionResponse();
    private Transaction transaction = new Transaction();
    private List<Transaction> transactions;

    private List<Portfolio> customerPortfolioList = new ArrayList<Portfolio>(){
        {add(portfolio);}};
    private List<Fund> customerFundList = new ArrayList<Fund>(){
        {add(fund);}};




    @BeforeClass
    public void setup() {

        super.setup();

        transaction.setAction(TransactionType.buy);
        transaction.setUnits(100);
        transaction.setFundId(1);

        transactions = new ArrayList<Transaction>(){{add(transaction);}};
    }

    @Test
    public void MissingHeader() {
        portfolio.setId(1);
        portfolio.setCustomerId("abc");
        recommendation.setRecommendationId(1);

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/modify")
                    .content(jpaJsonConverter.convertToDatabaseColumn(transactions))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
            //fail("Request should have failed");
        } catch (Exception e) {
            if(e.getClass() != MissingRequestHeaderException.class)
                fail("Received unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void MissingRequestBody() {
        portfolio.setId(1);
        portfolio.setCustomerId("abc");
        recommendation.setRecommendationId(1);

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/modify")
                    .header("x-custid", portfolio.getCustomerId())
            )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
            //fail("Request should have failed");
        } catch (Exception e) {
            if(e.getClass() != MissingServletRequestParameterException.class)
                fail("Received unexpected exception: " + e.getMessage());
        }
    }


    @Test
    public void throwsIfRecommendationIdIsNull() {
        portfolio.setId(1);
        portfolio.setCustomerId("abc");
        recommendation.setRecommendationId(null);
        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/modify")
                    .header("x-custid", portfolio.getCustomerId())
                    .content(jpaJsonConverter.convertToDatabaseColumn(transactions))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            fail("Received unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void throwsIfPortfolioIdDoesNotExist(){
        portfolio.setId(1);
        portfolio.setCustomerId("abc");
        recommendation.setPortfolioId(1);
        portfolioPreference.setPortfolioId(1);

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(portfolioPreference);
        when(fundSystemRequestService.getPortfolios(portfolio.getCustomerId())).thenReturn(Collections.emptyList());
        when(recommendationRepositoryService.findRecommendationByRecommendationId(recommendation.getRecommendationId()))
                .thenReturn(recommendation);

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/modify")
                    .header("x-custid", portfolio.getCustomerId())
                    .content(jpaJsonConverter.convertToDatabaseColumn(transactions))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("No Exception should be thrown");
        }
    }

    @Test//(expectedExceptions = ResourceNotFoundException.class)
    public void throwsIfPortfolioPreferenceDoesNotExist() {
        portfolio.setId(1);
        portfolio.setCustomerId("abc");

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(null);

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/modify")
                    .header("x-custid", portfolio.getCustomerId())
                    .content(jpaJsonConverter.convertToDatabaseColumn(transactions))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("No Exception should be thrown");
        }
    }

    @Test
    public void throwsIfRecommendationDoesNotExist() {
        portfolio.setId(1);
        portfolio.setCustomerId("abc");
        recommendation.setPortfolioId(1);
        portfolioPreference.setPortfolioId(1);

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(portfolioPreference);
        when(recommendationRepositoryService.findRecommendationByRecommendationId(recommendation.getRecommendationId()))
                .thenReturn(null);

        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/modify")
                    .header("x-custid", portfolio.getCustomerId())
                    .content(jpaJsonConverter.convertToDatabaseColumn(transactions))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("No Exception should be thrown");
        }
    }

    @Test
    public void normalRequest() {
        portfolio.setId(1);
        portfolio.setCustomerId("abc");
        fund.setFundId(1);
        fund.setFundName("stock");
        Holding holding = new Holding();
        holding.setFundId(1);
        holding.setUnits(50);
        portfolio.setHoldings(new ArrayList<Holding>(){{add(holding);}});
        recommendation.setPortfolioId(1);
        portfolioPreference.setPortfolioId(1);
        recommendation.setRecommendationId(1);
        Recommendation recommendation2 = new Recommendation();
        recommendation.setRecommendationId(1);

        recommendation2.setTransactions(transactions);
        recommendation2.setRecommendationId(1);

        when(portfolioRepositoryService.findPreferenceByPortfolioId(portfolio.getId())).thenReturn(portfolioPreference);
        when(fundSystemRequestService.getPortfolios(portfolio.getCustomerId())).thenReturn(new ArrayList<Portfolio>(){{add(portfolio);}});
        when(recommendationRepositoryService.findRecommendationByRecommendationId(recommendation.getRecommendationId())).thenReturn(recommendation);
        when(recommendationRepositoryService.updateRecommendationTransactions(eq(recommendation), anyList())).thenReturn(recommendation2);


        try{
            mockMvc.perform(MockMvcRequestBuilders.put("/roboadvisor/portfolio/" + portfolio.getId()
                    + "/recommendation/" + recommendation.getRecommendationId() + "/modify")
                    .header("x-custid", portfolio.getCustomerId())
                    .content(jpaJsonConverter.convertToDatabaseColumn(transactions))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.recommendationId", is(recommendation2.getRecommendationId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.transactions.length()", is(transactions.size())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].action", is(recommendation2.getTransactions().get(0).getAction().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].fundId", is(recommendation2.getTransactions().get(0).getFundId())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.transactions[0].units", is(recommendation2.getTransactions().get(0).getUnits())));
        } catch (Exception e) {
            fail("No Exception should be thrown");
        }
    }
}
