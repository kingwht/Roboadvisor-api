package com.hsbc.roboadvisor.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "portfolios")
public class Portfolio {

    @Id
    private String portfolioId;

    @DecimalMax("10.00")
    private BigDecimal deviation;

    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

    public Portfolio(String portfolioId, BigDecimal deviation, PortfolioType portfolioType) {
        this.portfolioId = portfolioId;
        this.deviation = deviation;
        this.portfolioType = portfolioType;
    }

}
