package com.hsbc.roboadvisor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @Range(min = 0)
    @Column(name = "portfolio_id")
    private Integer portfolioId;

    @NotNull
    @Column(name = "deviation")
    @Range(min = 0, max = 5)
    private Integer deviation;

    @NotNull
    @Column(name = "portfolio_type")
    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

    //TODO: Allocations

    public Portfolio(){
        //empty constructor
    }

    public Portfolio(Integer portfolioId, Integer deviation, PortfolioType portfolioType) {
        this.portfolioId = portfolioId;
        this.deviation = deviation;
        this.portfolioType = portfolioType;
    }

    public Integer getPortfolioId(){
        return this.portfolioId;
    }

    public void setPortfolioId(Integer portfolioId){
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
