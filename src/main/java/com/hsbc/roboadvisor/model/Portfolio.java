package com.hsbc.roboadvisor.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    private Long portfolioId;

    @Max(10) @Min(0)
    private Integer deviation;

    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

    public Portfolio(){
        //empty constructor
    }

    public Portfolio(Long portfolioId, Integer deviation, PortfolioType portfolioType) {
        this.portfolioId = portfolioId;
        this.deviation = deviation;
        this.portfolioType = portfolioType;
    }

    public Long getPortfolioId(){
        return this.portfolioId;
    }

    public void setPortfolioId(Long portfolioId){
        this.portfolioId = portfolioId;
    }

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
