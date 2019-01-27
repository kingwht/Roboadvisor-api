package com.hsbc.roboadvisor.payload;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.hsbc.roboadvisor.model.PortfolioType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Portfolio Preference Creation Request")
public class PortfolioRequest {

    @ApiModelProperty(required = true, value = "Acceptable Preference Deviation, must be between 0-5%")
    @Range(min = 0, max = 5)
    @NotNull(message = "Deviation may not be empty")
    private Integer deviation;

    @ApiModelProperty(required = true, value = "Portfolio Type", allowableValues = "FUND, CATEGORY")
    @NotNull
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
