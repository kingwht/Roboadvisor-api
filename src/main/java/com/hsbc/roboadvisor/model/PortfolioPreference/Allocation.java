package com.hsbc.roboadvisor.model.PortfolioPreference;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Allocation object which includes an asset and a preferred target percentage.")
public class Allocation implements Serializable
{
    @ApiModelProperty(value = "Fund Id for the specified allocation.")
    private Integer fundId;

    @ApiModelProperty(hidden = true, value = "Category for the specified allocation.")
    private Integer category;

    @ApiModelProperty(required = true, value = "Sum of all allocation percentages must total 100%")
    @NotNull
    private BigDecimal percentage;

    public Allocation() {
        //empty constructor
    }

    public Allocation(Integer fundId, Integer category, BigDecimal percentage) {
        this.fundId = fundId;
        this.category = category;
        this.percentage = percentage;
    }

    public Integer getFundId() {
        return this.fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public Integer getCategory() {
        return this.category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
