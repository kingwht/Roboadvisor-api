package com.hsbc.roboadvisor.model.Recommendation;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hsbc.roboadvisor.service.JpaJsonConverter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "recommendation")
@ApiModel(description = "Recommendation object which includes a recommendation Id, an associated portfolio Id and a list of transactions.")
public class Recommendation
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "recommendation_id")
    @ApiModelProperty(value = "Recommendation Id for the specified recommendation.")
    private Integer recommendationId;

    @NotNull
    @Column(name = "portfolio_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "The associated portfolio Id for the recommendation.")
    private Long portfolioId;

    @NotNull
    @Column(name = "transactions")
    @Convert(converter = JpaJsonConverter.class)
    @ApiModelProperty(value = "The list of suggested transactions for the recommendation.")
    private List<Transaction> transactionList;

    public Recommendation() {
        //empty constructor
    }

    public Recommendation(Long portfolioId, List<Transaction> transactionList) {
        this.portfolioId = portfolioId;
        this.transactionList = transactionList;
    }

    public Recommendation(Integer recommendationId, Long portfolioId, List<Transaction> transactionList) {
        this.recommendationId = recommendationId;
        this.portfolioId = portfolioId;
        this.transactionList = transactionList;
    }

    public Integer getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(Integer recommendationId) {
        this.recommendationId = recommendationId;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public List<Transaction> getTransactions() {
        return transactionList;
    }

    public void setTransactions(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
