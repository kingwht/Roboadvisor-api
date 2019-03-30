package com.hsbc.roboadvisor.service;

import static java.lang.Math.abs;
import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.math.BigDecimal.ROUND_HALF_EVEN;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.hsbc.roboadvisor.model.Recommendation.CategoryRecommendation;
import com.hsbc.roboadvisor.model.Recommendation.RecommendCategoryTransaction;
import com.hsbc.roboadvisor.model.Recommendation.Recommendation;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;
import com.hsbc.roboadvisor.model.Recommendation.TransactionType;
import com.hsbc.roboadvisor.repository.RecommendationRepository;

@Service
public class RecommendationRepositoryService
{
    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private FundRecommendationService fundRecommendationService;

    public Recommendation findRecommendationByRecommendationId(Integer recommendationId) {
        return this.recommendationRepository.findByRecommendationId(recommendationId);
    }

    public Recommendation findRecommendationByPortfolioId(String portfolioId) {
        return this.recommendationRepository.findByPortfolioId(portfolioId);
    }

    public Recommendation saveRecommendation(Portfolio portfolio,
            List<Fund> fundsList, PortfolioPreference portfolioPreference) {

        Map<Integer, BigDecimal> fundsMap = new HashMap<>(); //map of funds to current unit price
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

        for (int i = 0; i < portfolio.getHoldings().size(); i++) { //populate maps
            Holding holding = portfolio.getHoldings().get(i);
            totalValue = totalValue.add(holding.getBalance().getAmount());
            preferenceMap.put(fundPreferences.get(i).getFundId(), fundPreferences.get(i).getPercentage());
            fundUnitPriceMap.put(holding.getFundId(), fundsMap.get(holding.getFundId()));
            fundUnitCountMap.put(holding.getFundId(), holding.getUnits());
        }

        for (Holding holding : portfolio.getHoldings()) { //get percent occupied by each fund
            BigDecimal percent = holding.getBalance().getAmount()
                    .divide(totalValue, 10, ROUND_HALF_EVEN)
                    .multiply(new BigDecimal(100));
            fundPercentageMap.put(holding.getFundId(), percent);
        }

        for (Integer key : fundPercentageMap.keySet()) { //calculate required transactions
            BigDecimal percentDiff = preferenceMap.get(key).subtract(fundPercentageMap.get(key));
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

        Recommendation recommendation = new Recommendation(portfolioPreference.getPortfolioId(), transactions);

        return this.recommendationRepository.save(recommendation);
    }

    public CategoryRecommendation saveCategoryRecommentation(Portfolio portfolio,
            List<Fund> fundsList, PortfolioPreference portfolioPreference) {

        Map<Integer, BigDecimal> fundsMap = new HashMap<>(); //map of funds to current unit price
        Map<Integer, Integer> fundCategoryMap = new HashMap<>(); //map of fund to category
        for (Fund fund : fundsList) {
            fundsMap.put(fund.getFundId(), fund.getPrice().getAmount());
            fundCategoryMap.put(fund.getFundId(), fund.getCategory());
        }

        BigDecimal totalValue = new BigDecimal(0);
        Map<Integer, BigDecimal> fundUnitPriceMap = new HashMap<>(); //the unit price of each fund
        Map<Integer, Integer> fundUnitCountMap = new HashMap<>(); //how many of each unit owned per fund
        Map<Integer, BigDecimal> categoryAmountMap = new HashMap<>(); //total amount per category

        for (int i = 0; i < portfolio.getHoldings().size(); i++) {
            Holding holding = portfolio.getHoldings().get(i);
            Integer fundId = holding.getFundId();
            Integer category = fundCategoryMap.get(fundId);
            totalValue = totalValue.add(holding.getBalance().getAmount()); //update total
            fundUnitPriceMap.put(fundId, fundsMap.get(fundId)); //update unit price
            fundUnitCountMap.put(fundId, holding.getUnits()); //update unit count
            if (categoryAmountMap.containsKey(category)){ //update category amount
                BigDecimal amount = categoryAmountMap.get(category);
                categoryAmountMap.put(category, amount.add(holding.getBalance().getAmount()));
            } else {
                categoryAmountMap.put(category, holding.getBalance().getAmount());
            }
        }

        List<Allocation> categoryPref = portfolioPreference.getAllocations();
        Map<Integer, BigDecimal> categoryTargets = new HashMap<>();
        for (Allocation allocation : categoryPref) { //caculate required amount of change per category
            final Integer category = allocation.getCategory();
            BigDecimal occupiedCatPercent = categoryAmountMap.get(category).divide(totalValue, 10, ROUND_HALF_DOWN)
                    .multiply(new BigDecimal(100));
            BigDecimal categoryDifference = allocation.getPercentage().subtract(occupiedCatPercent);
            BigDecimal targetAmount = categoryDifference.multiply(totalValue)
                    .divide(new BigDecimal(100), 10, ROUND_HALF_DOWN);
            categoryTargets.put(category, targetAmount);
        }

        List<Transaction> transactions = new ArrayList<>();
        for (Holding holding : portfolio.getHoldings()) { //calculate transactions
            Integer fundId = holding.getFundId();
            Integer category = fundCategoryMap.get(fundId);
            BigDecimal categoryAmount = categoryAmountMap.get(category);
            BigDecimal proportion = holding.getBalance().getAmount().divide(categoryAmount, 10, ROUND_HALF_DOWN);
            BigDecimal targetAmount = categoryTargets.get(category);
            BigDecimal unitPrice = fundUnitPriceMap.get(fundId);
            BigDecimal totalUnitCost = targetAmount.divide(unitPrice, 10, RoundingMode.HALF_DOWN);

            int units = proportion.multiply(totalUnitCost).intValue();
            if ( units > 0 ) {
                transactions.add(new Transaction(TransactionType.buy, holding.getFundId(), units));
            }else if ( units < 0 ) {
                int amount = abs(units) > fundUnitCountMap.get(holding.getFundId())?
                        fundUnitCountMap.get(holding.getFundId()) : abs(units);
                transactions.add(new Transaction(TransactionType.sell, holding.getFundId(), amount));
            }
        }

        Recommendation recommendation = new Recommendation(portfolioPreference.getPortfolioId(), transactions);
        Recommendation result = this.recommendationRepository.save(recommendation);

        CategoryRecommendation categoryRecommendation = new CategoryRecommendation();
        categoryRecommendation.setRecommendation(result);

        List<RecommendCategoryTransaction> recommendCategoryTransactions = new ArrayList<>();
        for (Integer key : categoryTargets.keySet()) { //retrieve suggested ranked buy transactions
            if (categoryTargets.get(key).compareTo(new BigDecimal(0)) == 1) {
                RecommendCategoryTransaction recTransaction = new RecommendCategoryTransaction();
                recTransaction.setCategory(key);
                recTransaction.setTransactions(this.fundRecommendationService
                        .getRecommendedTransactions(fundsList, key, categoryTargets.get(key)));
                recommendCategoryTransactions.add(recTransaction);
            }
        }
        categoryRecommendation.setRecommendedTransactionList(recommendCategoryTransactions);
        return categoryRecommendation;
    }

    public Recommendation updateRecommendationTransactions(Recommendation recommendation, List<Transaction> transactionList) {
        recommendation.setTransactions(transactionList);

        return this.recommendationRepository.save(recommendation);
    }
}
