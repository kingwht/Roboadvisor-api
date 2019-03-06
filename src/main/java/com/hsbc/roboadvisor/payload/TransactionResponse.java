package com.hsbc.roboadvisor.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.roboadvisor.model.Fund.TransactionDetail;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    @NotNull
    private Integer status;

    @NotNull
    private Integer portfolioId;

    @NotNull
    private Integer transactionId;

    @NotNull
    private List<TransactionDetail> transactions;

    public Integer getStatus() {
        return status;
    }

    public Integer getPortfolioId() {
        return portfolioId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public List<TransactionDetail> getTransactions() {
        return transactions;
    }
}
