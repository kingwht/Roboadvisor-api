package com.hsbc.roboadvisor.model.Portfolio;

import java.io.Serializable;
import java.util.List;

public class Portfolio implements Serializable
{
    private String customerId;

    private String id;

    private List<Holding> holdings;

    public Portfolio() {
        //empty constructor
    }

    public Portfolio(String customerId, String id, List<Holding> holdings) {
        this.customerId = customerId;
        this.id = id;
        this.holdings = holdings;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Holding> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<Holding> holdings) {
        this.holdings = holdings;
    }
}
