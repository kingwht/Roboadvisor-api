package com.hsbc.roboadvisor.payload;

import java.util.List;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.roboadvisor.model.Fund.TransactionDetail;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    @NotNull
    private Integer status;

    @NotNull
    private String portfolioId;

    @NotNull
    private Integer transactionId;

    @NotNull
    private List<TransactionDetail> transactions;

    public Integer getStatus() {
        return status;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public List<TransactionDetail> getTransactions() {
        return transactions;
    }
}
