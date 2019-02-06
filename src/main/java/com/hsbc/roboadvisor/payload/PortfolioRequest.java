package com.hsbc.roboadvisor.payload;

import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.roboadvisor.model.Allocation;
import com.hsbc.roboadvisor.model.PortfolioType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Portfolio Preference Creation Request")
public class PortfolioRequest {

    @ApiModelProperty(required = true, value = "Acceptable Preference Deviation, must be between 0-5%")
    @Range(min = 0, max = 5)
    @NotNull(message = "Deviation may not be empty")
    private Integer deviation;

    @ApiModelProperty(required = true, value = "Portfolio Type", allowableValues = "fund, category")
    @NotNull
    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

    @ApiModelProperty(required = true, value = "Asset Allocations")
    @NotNull
    private List<Allocation> allocations;

    public PortfolioRequest() {
        //empty constructor
    }

    public PortfolioRequest(Integer deviation, PortfolioType portfolioType) {
        this.deviation = deviation;
        this.portfolioType = portfolioType;
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

    public List<Allocation> getAllocations() {
        return this.allocations;
    }

    public void setAllocations(List<Allocation> allocations) {
        this.allocations = allocations;
    }
}
