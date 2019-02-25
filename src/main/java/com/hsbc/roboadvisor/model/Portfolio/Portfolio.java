package com.hsbc.roboadvisor.model.Portfolio;

import java.io.Serializable;
import java.util.List;

public class Portfolio implements Serializable
{
    private Integer id;

    private List<Holding> holdings;

    public Portfolio() {
        //empty constructor
    }

    public Portfolio(Integer id, List<Holding> holdings) {
        this.id = id;
        this.holdings = holdings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Holding> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<Holding> holdings) {
        this.holdings = holdings;
    }
}
