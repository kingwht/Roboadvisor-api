package com.hsbc.roboadvisor.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;

@Repository
public interface PortfolioRepository extends CrudRepository<PortfolioPreference, String>
{
    PortfolioPreference findByPortfolioId(String portfolioId);

    Boolean existsByPortfolioId(String portfolioId);
    
}
