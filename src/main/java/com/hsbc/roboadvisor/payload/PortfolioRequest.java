package com.hsbc.roboadvisor.payload;

import java.math.BigDecimal;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import com.hsbc.roboadvisor.model.PortfolioType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PortfolioRequest {

    @DecimalMax("10.00")
    @DecimalMin("0.00")
    private BigDecimal deviation;

    @Enumerated(EnumType.STRING)
    private PortfolioType portfolioType;

}
