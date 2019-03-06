package com.hsbc.roboadvisor.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequest {

    @NotNull
    private Integer portfolioId;

    @NotNull
    private List<Transaction> instructions;

    public Integer getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Integer portfolioId) {
        this.portfolioId = portfolioId;
    }

    public List<Transaction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Transaction> instructions) {
        this.instructions = instructions;
    }
}
