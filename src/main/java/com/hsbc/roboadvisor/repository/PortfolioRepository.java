package com.hsbc.roboadvisor.repository;

import com.hsbc.roboadvisor.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, String> {

    Portfolio findByPortfolioId(String portfolioId);

    Boolean existsByPortfolioId(String portfolioId);
    
}