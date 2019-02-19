package com.hsbc.roboadvisor.model;

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
    private Integer portfolioId;

    @NotNull
    @Column(name = "deviation")
    @Range(min = 0, max = 5)
    private Integer deviation;

    @NotNull
    @Column(name = "portfolio_type")
    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

    @NotNull
    @Column(name = "allocations")
    @Convert(converter = JpaJsonConverter.class)
    private List<Allocation> allocations;

    public PortfolioPreference(){
        //empty constructor
    }

    public PortfolioPreference(Integer portfolioId, Integer deviation, PortfolioType portfolioType, List<Allocation> allocations) {
        this.portfolioId = portfolioId;
        this.deviation = deviation;
        this.portfolioType = portfolioType;
        this.allocations = allocations;
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

    public List<Allocation> getAllocations() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this.allocations, new TypeReference<List<Allocation>>() {});
    }

    public void setAllocations(List<Allocation> allocations) {
        this.allocations = allocations;
    }
}
