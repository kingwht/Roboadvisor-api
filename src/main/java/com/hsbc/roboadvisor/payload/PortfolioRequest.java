package com.hsbc.roboadvisor.payload;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.hsbc.roboadvisor.model.PortfolioType;

public class PortfolioRequest {

    @Max(10) @Min(0)
    private Integer deviation;

    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

    public Integer getDeviation(){
        return this.deviation;
    }

    public void setDeviation(Integer deviation){
        this.deviation = deviation;
    }

    public PortfolioType getPortfolioType(){
        return this.portfolioType;
    }

    public void setPortfolioType(PortfolioType portfolioType){
        this.portfolioType = portfolioType;
    }

}
