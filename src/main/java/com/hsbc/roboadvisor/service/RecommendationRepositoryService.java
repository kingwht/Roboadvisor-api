package com.hsbc.roboadvisor.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsbc.roboadvisor.model.PortfolioPreference;
import com.hsbc.roboadvisor.model.Recommendation;
import com.hsbc.roboadvisor.model.Transaction;
import com.hsbc.roboadvisor.model.TransactionType;
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

    public Recommendation saveRecommendation(Object portfolio, PortfolioPreference portfolioPreference) {

        //TODO: Insert algorithm to create new transactions using portfolio


        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction(TransactionType.buy, 3, 300); //TODO: STUB
        transactions.add(transaction);
        Recommendation recommendation = new Recommendation(portfolioPreference.getPortfolioId(), transactions);

        return this.recommendationRepository.save(recommendation);
    }

    public Recommendation updateRecommendation(Recommendation recommendation, Object portfolio, PortfolioPreference portfolioPreference) {
        //TODO: Insert algorithm to create new transactions using portfolio

        List<Transaction> transactions = new ArrayList<>();
        Transaction t1 = new Transaction(TransactionType.sell, 5, 500); //TODO: STUB
        Transaction t2 = new Transaction(TransactionType.buy, 10, 100); //TODO: STUB
        transactions.add(t1);
        transactions.add(t2);

        recommendation.setTransactions(transactions);

        return this.recommendationRepository.save(recommendation);

    }

    public Recommendation updateRecommendationTransactions(Recommendation recommendation, List<Transaction> transactionList) {
        recommendation.setTransactions(transactionList);

        return  this.recommendationRepository.save(recommendation);
    }
}
