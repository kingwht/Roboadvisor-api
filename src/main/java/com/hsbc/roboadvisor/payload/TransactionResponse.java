package com.hsbc.roboadvisor.payload;

import java.util.List;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.roboadvisor.model.Fund.TransactionDetail;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    @NotNull
    private Integer status;

    private Long portfolioId;

    private Long transactionId;

    @NotNull
    private List<TransactionDetail> transactions;

    public Integer getStatus() {
        return status;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public List<TransactionDetail> getTransactions() {
        return transactions;
    }
}
