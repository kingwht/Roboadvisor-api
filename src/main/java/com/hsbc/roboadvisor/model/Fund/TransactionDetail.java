package com.hsbc.roboadvisor.model.Fund;

import com.hsbc.roboadvisor.model.Common.CurrencyAmount;
import com.hsbc.roboadvisor.model.Recommendation.TransactionType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TransactionDetail {

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType action;

    @NotNull
    private Integer fundId;

    @NotNull
    @Min(value = 0)
    private Integer units;

    @NotNull
    private CurrencyAmount price;

    public TransactionType getAction() {
        return action;
    }

    public void setAction(TransactionType action) {
        this.action = action;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public CurrencyAmount getPrice() {
        return price;
    }

    public void setPrice(CurrencyAmount price) {
        this.price = price;
    }
}
