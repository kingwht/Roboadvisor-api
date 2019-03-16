package com.hsbc.roboadvisor.service;

import static java.lang.Math.abs;
import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ROUND_HALF_EVEN;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Portfolio.Holding;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;
import com.hsbc.roboadvisor.model.PortfolioPreference.Allocation;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.model.Recommendation.Recommendation;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;
import com.hsbc.roboadvisor.model.Recommendation.TransactionType;
import com.hsbc.roboadvisor.repository.RecommendationRepository;

@Service
public class RecommendationRepositoryService
{
    @Autowired
    private RecommendationRepository recommendationRepository;

    public Boolean recommendationExistsByRecommendationId(Integer recommendationId) {
        return this.recommendationRepository.existsByPortfolioId(recommendationId);
    }

    public Recommendation findRecommendationByRecommendationId(Integer recommendationId) {
        return this.recommendationRepository.findByRecommendationId(recommendationId);
    }

    public Recommendation findRecommendationByPortfolioId(Integer portfolioId) {
        return this.recommendationRepository.findByPortfolioId(portfolioId);
    }

    public Recommendation saveRecommendation(Recommendation recommendation, Portfolio portfolio,
            List<Fund> fundsList, PortfolioPreference portfolioPreference) {

        Map<Integer, BigDecimal> fundsMap = new HashMap<>(); //map of funds to unit price
        for (Fund fund : fundsList) {
            fundsMap.put(fund.getFundId(), fund.getPrice().getAmount());
        }

        List<Allocation> fundPreferences = portfolioPreference.getAllocations();
        BigDecimal totalValue = new BigDecimal(0);
        Map<Integer, BigDecimal> preferenceMap = new HashMap<>(); //the preferred percent for each fund
        Map<Integer, BigDecimal> fundPercentageMap = new HashMap<>(); //how many percent a fund occupies
        Map<Integer, BigDecimal> fundUnitPriceMap = new HashMap<>(); //the unit price of each fund
        Map<Integer, Integer> fundUnitCountMap = new HashMap<>(); //how many of each unit owned per fund
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < portfolio.getHoldings().size(); i++) {
            Holding holding = portfolio.getHoldings().get(i);
            totalValue = totalValue.add(holding.getBalance().getAmount());
            preferenceMap.put(fundPreferences.get(i).getFundId(), fundPreferences.get(i).getPercentage());
            fundUnitPriceMap.put(holding.getFundId(), fundsMap.get(holding.getFundId()));
            fundUnitCountMap.put(holding.getFundId(), holding.getUnits());
        }

        for (Holding holding : portfolio.getHoldings()) {
            BigDecimal percent = holding.getBalance().getAmount()
                    .divide(totalValue, 10, ROUND_HALF_EVEN)
                    .multiply(new BigDecimal(100));
            fundPercentageMap.put(holding.getFundId(), percent);
        }

        for (Integer key : fundPercentageMap.keySet()) {
            BigDecimal percentDiff = preferenceMap.get(key).subtract(fundPercentageMap.get(key));
            if ( percentDiff.abs().compareTo(new BigDecimal(portfolioPreference.getDeviation())) == 1) {
                int units = percentDiff.divide(new BigDecimal(100), 10, ROUND_HALF_EVEN)
                        .multiply(totalValue.setScale(10, ROUND_HALF_EVEN))
                        .divide(fundUnitPriceMap.get(key),10, ROUND_HALF_EVEN)
                        .setScale(0, ROUND_FLOOR).intValue();

                if ( units > 0 ) {
                    transactions.add(new Transaction(TransactionType.buy, key, units));
                }else if ( units < 0 ) {
                    int amount = abs(units) > fundUnitCountMap.get(key)? fundUnitCountMap.get(key) : abs(units);
                    transactions.add(new Transaction(TransactionType.sell, key, amount));
                }
            }
        }

        if (recommendation == null) {
            recommendation = new Recommendation(portfolioPreference.getId(), transactions);
        } else {
            recommendation.setTransactions(transactions);
        }

        return this.recommendationRepository.save(recommendation);
    }

    public Recommendation updateRecommendationTransactions(Recommendation recommendation, List<Transaction> transactionList) {
        recommendation.setTransactions(transactionList);

        return  this.recommendationRepository.save(recommendation);
    }
}
