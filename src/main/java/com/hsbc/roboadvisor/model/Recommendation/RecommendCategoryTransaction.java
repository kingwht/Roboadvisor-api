package com.hsbc.roboadvisor.model.Recommendation;

import java.util.List;

public class RecommendCategoryTransaction
{
    private Integer category;

    private List<Transaction> transactions;

    public RecommendCategoryTransaction() {
        // empty constructor
    }

    public RecommendCategoryTransaction( Integer category, List<Transaction> transactions) {
        this.category = category;
        this.transactions = transactions;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
