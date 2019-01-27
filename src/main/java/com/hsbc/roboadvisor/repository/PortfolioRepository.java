package com.hsbc.roboadvisor.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hsbc.roboadvisor.model.Portfolio;

@Repository
public interface PortfolioRepository extends CrudRepository<Portfolio, String>
{

    Portfolio findByPortfolioId(Integer portfolioId);

    Boolean existsByPortfolioId(Integer portfolioId);
    
}
