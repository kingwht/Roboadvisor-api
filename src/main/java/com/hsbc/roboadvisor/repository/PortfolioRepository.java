package com.hsbc.roboadvisor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hsbc.roboadvisor.model.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, String> {

    Portfolio findByPortfolioId(String portfolioId);

    Boolean existsByPortfolioId(int portfolioId);
    
}
