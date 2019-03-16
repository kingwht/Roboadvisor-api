package com.hsbc.roboadvisor.model.PortfolioPreference;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.roboadvisor.service.JpaJsonConverter;

import io.swagger.annotations.ApiModel;

@Entity
@Table(name = "portfolio")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Portfolio Preference Object")
public class PortfolioPreference {

    @Id
    @Range(min = 0)
    @Column(name = "portfolio_id")
    private Integer id;

    @NotNull
    @Column(name = "deviation")
    @Range(min = 0, max = 5)
    private Integer deviation;

    @NotNull
    @Column(name = "portfolio_type")
    @Enumerated(EnumType.STRING)
    private PortfolioType type;

    @NotNull
    @Column(name = "allocations")
    @Convert(converter = JpaJsonConverter.class)
    private List<Allocation> allocations;

    public PortfolioPreference(){
        //empty constructor
    }

    public PortfolioPreference(Integer id, Integer deviation, PortfolioType type, List<Allocation> allocations) {
        this.id = id;
        this.deviation = deviation;
        this.type = type;
        this.allocations = allocations;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getDeviation(){
        return this.deviation;
    }

    public void setDeviation(Integer deviation){
        this.deviation = deviation;
    }

    public PortfolioType getType(){
        return this.type;
    }

    public void setType(PortfolioType type){
        this.type = type;
    }

    public List<Allocation> getAllocations() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this.allocations, new TypeReference<List<Allocation>>() {});
    }

    public void setAllocations(List<Allocation> allocations) {
        this.allocations = allocations;
    }
}
