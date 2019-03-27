package com.hsbc.roboadvisor.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hsbc.roboadvisor.exception.BadRequestException;
import com.hsbc.roboadvisor.model.Fund.AssetComposition;
import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;
import com.hsbc.roboadvisor.model.Recommendation.TransactionType;

@Service
public class FundRecommendationService
{
    private static final Logger _logger = LoggerFactory.getLogger(FundRecommendationService.class);

    private Map<String, Double> fundYearPerformanceWeight = new HashMap<>();

    private Map<String, Double> sectorOneYearPerformanceWeight = new HashMap<>();

    private static final String THREE_MONTH = "3m";
    private static final String SIX_MONTH = "6m";
    private static final String NINE_MONTH = "9m";
    private static final String ONE_YEAR = "1y";
    private static final String THREE_YEAR = "3y";
    private static final String FIVE_YEAR = "5y";
    private static final String TEN_YEAR = "10y";

    private static final String INFO_TECH = "info_tech";
    private static final String CONSUMER_STAPLES = "c_services";
    private static final String ENERGY = "energy";
    private static final String MATERIALS = "materials";
    private static final String UTILITIES = "utilities";
    private static final String TELECOM_SERVICES = "communications";
    private static final String FINANCIALS = "financials";
    private static final String REAL_ESTATE = "real_estate";
    private static final String INDUSTRIALS = "industrials";
    private static final String HEALTHCARE = "healthcare";
    private static final String CONSUMER_DISCRETIONARY = "c_discretionary";

    @PostConstruct
    public void init(){
        _logger.info("Fund Recommendation Service Init!");

        setFundYearPerformanceWeight();
        setSectorOneYearPerformanceWeight();

    }

    private void setFundYearPerformanceWeight() {
        fundYearPerformanceWeight.put(THREE_MONTH, 0.8);
        fundYearPerformanceWeight.put(SIX_MONTH, 0.85);
        fundYearPerformanceWeight.put(NINE_MONTH, 0.9);
        fundYearPerformanceWeight.put(ONE_YEAR, 1.0);
        fundYearPerformanceWeight.put(THREE_YEAR, 1.1);
        fundYearPerformanceWeight.put(FIVE_YEAR, 1.15);
        fundYearPerformanceWeight.put(TEN_YEAR, 1.2);
    }

    private void setSectorOneYearPerformanceWeight() {
        /*
        Barchart's free API does not support requesting for sector data. Their GetQuotes endpoint
         only supports listed stocks and not market sector indexes. Updated sector data would require
         their OnDemand API, but will render a subscription cost.
        */

        sectorOneYearPerformanceWeight.put(INFO_TECH, 20.83);
        sectorOneYearPerformanceWeight.put(CONSUMER_STAPLES, 16.56);
        sectorOneYearPerformanceWeight.put(ENERGY, -11.16);
        sectorOneYearPerformanceWeight.put(MATERIALS, 2.7);
        sectorOneYearPerformanceWeight.put(UTILITIES, 8.14);
        sectorOneYearPerformanceWeight.put(TELECOM_SERVICES, 16.22);
        sectorOneYearPerformanceWeight.put(FINANCIALS, 0.29);
        sectorOneYearPerformanceWeight.put(REAL_ESTATE, 14.79);
        sectorOneYearPerformanceWeight.put(INDUSTRIALS, 11.33);
        sectorOneYearPerformanceWeight.put(HEALTHCARE, 48.67);
        sectorOneYearPerformanceWeight.put(CONSUMER_DISCRETIONARY, -5.7);
    }

    // Returns list of suggest transactions for a given fund category within the budget constraint
    public List<Transaction> getRecommendedTransactions(List<Fund> customerFundList, Integer fundCategory, BigDecimal budget){

        List<Fund> fundsOfCategory = new ArrayList<>();
        for (Fund fund : customerFundList) {
            if (fund.getCategory().equals(fundCategory)) {
                fundsOfCategory.add(fund);
            }
        }

        if (fundsOfCategory.size() == 0) {
            throw new BadRequestException("No funds exists for the requested category");
        }

        Map<Integer, Double> fundScoreMap = new HashMap<>();
        Map<Integer, Transaction> fundTransactionMap = new HashMap<>();

        for (Fund fund : fundsOfCategory) {
            Double score = calculatePerformanceScore(fund);
            fundScoreMap.put(fund.getFundId(), score);

            Transaction transaction = suggestTransaction(fund, budget);
            fundTransactionMap.put(fund.getFundId(), transaction);
        }

        _logger.info("Scores per fund {}", fundScoreMap.toString());

        Map<Integer, Double> sortedFundScoreMap = fundScoreMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        List<Transaction> transactionList = new ArrayList<>();

        for (Integer fundId : sortedFundScoreMap.keySet()) {
            transactionList.add(fundTransactionMap.get(fundId));
        }

        return transactionList;
    }

    private Double calculatePerformanceScore(Fund fund) {
        Double sectorScore = 0.0;
        Double averageReturnScore = 0.0;

        for (AssetComposition assetComposition : fund.getAssetComposition()) {
            sectorScore += (assetComposition.getPercentage() / 100.0) * (sectorOneYearPerformanceWeight.get(assetComposition.getGics()));
        }

        for (String key : fund.getAverageReturns().keySet()) {
            averageReturnScore += (fund.getAverageReturns().get(key) / 10.0 ) * (fundYearPerformanceWeight.get(key));
        }

        return sectorScore + averageReturnScore;
    }

    private Transaction suggestTransaction(Fund fund, BigDecimal budget) {
        Transaction transaction = new Transaction();
        transaction.setAction(TransactionType.buy);
        transaction.setFundId(fund.getFundId());

        Integer units = budget.divide(fund.getPrice().getAmount(), RoundingMode.FLOOR).intValue();
        transaction.setUnits(units);

        _logger.info("For Fund {}: Buy {} units.", fund.getFundId(), units);
        return transaction;
    }

}
