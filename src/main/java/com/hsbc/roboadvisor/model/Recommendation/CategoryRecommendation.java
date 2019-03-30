package com.hsbc.roboadvisor.model.Recommendation;

import java.util.List;

public class CategoryRecommendation
{
    private Recommendation                     recommendation;
    private List<RecommendCategoryTransaction> recommendedTransactionList;

    public CategoryRecommendation() {
        // empty constructor
    }

    public CategoryRecommendation(Recommendation recommendation, List<RecommendCategoryTransaction> recommendedTransactionList) {
        this.recommendation = recommendation;
        this.recommendedTransactionList = recommendedTransactionList;
    }

    public List<RecommendCategoryTransaction> getRecommendedTransactionList() {
        return recommendedTransactionList;
    }

    public void setRecommendedTransactionList(List<RecommendCategoryTransaction> recommendedTransactionList) {
        this.recommendedTransactionList = recommendedTransactionList;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }
}
