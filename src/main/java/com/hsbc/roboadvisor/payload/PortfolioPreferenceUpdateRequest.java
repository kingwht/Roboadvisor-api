package com.hsbc.roboadvisor.payload;

import java.util.List;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.roboadvisor.model.PortfolioPreference.Allocation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "PortfolioPreference Preference Update Request")
public class PortfolioPreferenceUpdateRequest
{

    @ApiModelProperty(required = true, value = "Acceptable Preference Deviation, must be between 0-5%")
    @Range(min = 0, max = 5)
    @NotNull(message = "Deviation may not be empty")
    private Integer deviation;

    @ApiModelProperty(required = true, value = "Asset Allocations")
    @NotNull
    private List<Allocation> allocations;

    public PortfolioPreferenceUpdateRequest() {
        //empty constructor
    }

    public PortfolioPreferenceUpdateRequest(Integer deviation, List<Allocation> allocations) {
        this.deviation = deviation;
        this.allocations = allocations;
    }

    public Integer getDeviation(){
        return this.deviation;
    }

    public void setDeviation(Integer deviation){
        this.deviation = deviation;
    }

    public List<Allocation> getAllocations() {
        return this.allocations;
    }

    public void setAllocations(List<Allocation> allocations) {
        this.allocations = allocations;
    }
}
