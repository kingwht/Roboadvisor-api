package com.hsbc.roboadvisor.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    private String portfolioId;

    @DecimalMax("10.00")
    private BigDecimal deviation;

    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

    public Portfolio() {
        // empty constructor
    }

    public Portfolio(String portfolioId, BigDecimal deviation, PortfolioType portfolioType) {
        this.portfolioId = portfolioId;
        this.deviation = deviation;
        this.portfolioType = portfolioType;
    }

    public String getId() {
        return this.portfolioId;
    }

    public void setId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public BigDecimal getDeviation() {
        return this.deviation;
    }

    public void setDeviation(BigDecimal deviation) {
        this.deviation = deviation;
    }

    public PortfolioType getPortfolioType() {
        return this.portfolioType;
    }

    public void setPortfolioType(PortfolioType portfolioType) {
        this.portfolioType = portfolioType;
    }

}