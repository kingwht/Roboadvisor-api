package com.hsbc.roboadvisor.model;

import java.math.BigDecimal;

public class Volume
{
    private BigDecimal amount;

    private Currency currency;

    public Volume() {
        //empty constructor
    }

    public Volume(BigDecimal amount, Currency currency) {
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
