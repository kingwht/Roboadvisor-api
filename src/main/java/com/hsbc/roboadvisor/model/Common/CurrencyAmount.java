package com.hsbc.roboadvisor.model.Common;

import java.math.BigDecimal;

import com.hsbc.roboadvisor.model.Portfolio.Currency;

public class CurrencyAmount
{
    private BigDecimal amount;

    private Currency currency;

    public CurrencyAmount() {
        //empty constructor
    }

    public CurrencyAmount(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
