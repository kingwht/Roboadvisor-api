package com.hsbc.roboadvisor.service;

import com.hsbc.roboadvisor.model.Common.CurrencyAmount;
import com.hsbc.roboadvisor.model.Fund.AssetComposition;
import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Portfolio.Currency;
import com.hsbc.roboadvisor.model.Portfolio.Holding;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;
import com.hsbc.roboadvisor.model.PortfolioPreference.Allocation;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioType;
import com.hsbc.roboadvisor.model.Recommendation.Recommendation;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;
import com.hsbc.roboadvisor.model.Recommendation.TransactionType;
import com.hsbc.roboadvisor.repository.RecommendationRepository;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RecommendationServiceTest {

    // Example Models
    private Holding holding1 = new Holding(9123, 100, new CurrencyAmount(new BigDecimal(500), Currency.CAD));
    private Holding holding2 = new Holding(9124, 150, new CurrencyAmount(new BigDecimal(1500), Currency.CAD));
    private List<Holding> holdings = new ArrayList<Holding>(){{add(holding1); add(holding2);}};
    private Portfolio portfolio = new Portfolio("abcde", 12345, holdings);
    //private List<Portfolio> portfolios  = new ArrayList<>();

    private Allocation allocation1 = new Allocation(9123, 1, new BigDecimal(50));
    private Allocation allocation2 = new Allocation(9124, 1, new BigDecimal(50));
    private List<Allocation> allocations = new ArrayList<Allocation>(){{add(allocation1); add(allocation2);}};;

    private PortfolioPreference portfolioPreference = new PortfolioPreference(12345, 5, PortfolioType.fund, allocations);


    private Fund fund1 = new Fund(null, 9123, null, 1,
            new CurrencyAmount(new BigDecimal(5), Currency.CAD), "silver");

    private Fund fund2 = new Fund(null, 9124, null, 1,
            new CurrencyAmount(new BigDecimal(10), Currency.CAD), "gold");

    private List<Fund> funds = new ArrayList<Fund>(){{add(fund1); add(fund2);}};


    @InjectMocks
    private RecommendationRepositoryService recommendationRepositoryService;

    @Mock private RecommendationRepository recommendationRepository;

    @BeforeClass
    public void setup() {

        MockitoAnnotations.initMocks(this);

        when(recommendationRepository.save(any(Recommendation.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return invocation.getArguments()[0];
            }
        });
    }

    @Test
    public void normalTest () {

        Recommendation recommendation = recommendationRepositoryService.saveRecommendation(portfolio, funds, portfolioPreference);
        Assert.assertEquals(recommendation.getPortfolioId(), portfolio.getId());
        Assert.assertEquals(recommendation.getTransactions().size(), 2);

        Transaction transaction1 = recommendation.getTransactions().get(0);
        Assert.assertEquals(transaction1.getFundId(), fund1.getFundId());
        Assert.assertEquals(transaction1.getAction(), TransactionType.buy);
        Assert.assertEquals((int)transaction1.getUnits(), 100);

        Transaction transaction2 = recommendation.getTransactions().get(1);
        Assert.assertEquals(transaction2.getFundId(), fund2.getFundId());
        Assert.assertEquals(transaction2.getAction(), TransactionType.sell);
        Assert.assertEquals((int)transaction2.getUnits(), 50);
    }

    @Test
    public void testWithThreeFund () {
        Fund fund3 = new Fund(null, 9125, null, 1,
                new CurrencyAmount(new BigDecimal(2), Currency.CAD), "copper");

        Holding holding3 = new Holding(9125, 1000, new CurrencyAmount(new BigDecimal(2000), Currency.CAD));
        Allocation allocation3 = new Allocation(9125, 1, new BigDecimal(33));
        allocation1.setPercentage(new BigDecimal(33));
        allocation2.setPercentage(new BigDecimal(34));

        holdings.add(holding3);
        funds.add(fund3);
        allocations.add(allocation3);


        Recommendation recommendation = recommendationRepositoryService.saveRecommendation(portfolio, funds, portfolioPreference);
        Assert.assertEquals(recommendation.getPortfolioId(), portfolio.getId());
        System.out.println("porfolio info:" + new JpaJsonConverter().convertToDatabaseColumn(portfolio) + "\n");
        System.out.println("porfolioPreference info:" + new JpaJsonConverter().convertToDatabaseColumn(portfolioPreference)+ "\n");
        System.out.println("funds info:" + new JpaJsonConverter().convertToDatabaseColumn(funds)+ "\n");
        System.out.println("recommendation info:" + new JpaJsonConverter().convertToDatabaseColumn(recommendation));
        Assert.assertEquals(recommendation.getTransactions().size(), 3);

        Transaction transaction1 = recommendation.getTransactions().get(0);
        Assert.assertEquals(transaction1.getFundId(), fund1.getFundId());
        Assert.assertEquals(transaction1.getAction(), TransactionType.buy);
        Assert.assertEquals((int)transaction1.getUnits(), 100);

        Transaction transaction2 = recommendation.getTransactions().get(1);
        Assert.assertEquals(transaction2.getFundId(), fund2.getFundId());
        Assert.assertEquals(transaction2.getAction(), TransactionType.sell);
        Assert.assertEquals((int)transaction2.getUnits(), 50);

        Transaction transaction3 = recommendation.getTransactions().get(2);
        Assert.assertEquals(transaction3.getFundId(), fund2.getFundId());
        Assert.assertEquals(transaction3.getAction(), TransactionType.sell);
        Assert.assertEquals((int)transaction3.getUnits(), 50);
    }



}
