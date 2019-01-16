package com.hsbc.roboadvisor.payload;

import java.math.BigDecimal;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.hsbc.roboadvisor.model.PortfolioType;

public class PortfolioRequest {

    @DecimalMax("10.00")
    @DecimalMin("0.00")
    private BigDecimal deviation;

    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

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