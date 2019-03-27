package com.hsbc.roboadvisor.payload;


import java.util.List;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequest {

    @NotNull
    private Long portfolioId;

    @NotNull
    private List<Transaction> instructions;

    public TransactionRequest() {
    }

    public TransactionRequest(@NotNull Long portfolioId, @NotNull List<Transaction> instructions) {
        this.portfolioId = portfolioId;
        this.instructions = instructions;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public List<Transaction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Transaction> instructions) {
        this.instructions = instructions;
    }
}
