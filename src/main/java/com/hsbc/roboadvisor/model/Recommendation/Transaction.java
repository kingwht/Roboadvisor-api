package com.hsbc.roboadvisor.model.Recommendation;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Transaction Object")
public class Transaction
{
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType action;

    private Long fundId;

    @NotNull
    @Min(value = 0)
    private Integer units;

    public Transaction() {
        //empty constructor
    }

    public Transaction(TransactionType transactionType, Long fundId, Integer units) {
        this.action = transactionType;
        this.fundId = fundId;
        this.units = units;
    }

    public TransactionType getAction() {
        return action;
    }

    public void setAction(TransactionType transactionType) {
        this.action = transactionType;
    }

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }
}
