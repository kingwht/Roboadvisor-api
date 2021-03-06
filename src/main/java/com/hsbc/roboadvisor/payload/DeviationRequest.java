package com.hsbc.roboadvisor.payload;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Update Portfolio Preference Deviation Request")
public class DeviationRequest
{
    @ApiModelProperty(required = true, value = "Acceptable Preference Deviation, must be between 0-5%")
    @Range(min = 0, max = 5)
    @NotNull(message = "Deviation may not be empty")
    private Integer deviation;

    public DeviationRequest() {
        //empty constructor
    }

    public DeviationRequest(Integer deviation) {
        this.deviation = deviation;
    }

    public Integer getDeviation(){
        return this.deviation;
    }

    public void setDeviation(Integer deviation){
        this.deviation = deviation;
    }
}
