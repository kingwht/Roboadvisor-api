package com.hsbc.roboadvisor.payload;

import java.util.List;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsbc.roboadvisor.model.Allocation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Update Portfolio Allocations")
public class AllocationsRequest
{
    @ApiModelProperty(required = true, value = "List of Allocations. Sum of percentage must total 100%.")
    @NotNull(message = "List of Allocations may not be empty")
    private List<Allocation> allocations;

    public AllocationsRequest() {
        //empty constructor
    }

    public AllocationsRequest(List<Allocation> allocations) {
        this.allocations = allocations;
    }

    public List<Allocation> getAllocations(){
        return this.allocations;
    }

    public void setAllocations(List<Allocation> allocations){
        this.allocations = allocations;
    }
}
