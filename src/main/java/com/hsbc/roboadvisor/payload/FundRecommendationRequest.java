package com.hsbc.roboadvisor.payload;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Fund Recommendation Request")
public class FundRecommendationRequest
{
    @NotNull(message = "fundCategory may not be empty")
    private Integer fundCategory;

    @NotNull(message = "Budget may not be empty")
    @Min(value = 1)
    private BigDecimal budget;


    public FundRecommendationRequest() {
        //empty constructor
    }

    public FundRecommendationRequest(Integer fundCategory, BigDecimal budget) {
        this.fundCategory = fundCategory;
        this.budget = budget;
    }

    public Integer getFundCategory(){
        return this.fundCategory;
    }

    public void setFundCategory(Integer fundCategory){
        this.fundCategory = fundCategory;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}
