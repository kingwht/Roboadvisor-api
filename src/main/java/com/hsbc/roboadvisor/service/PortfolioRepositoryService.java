package com.hsbc.roboadvisor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsbc.roboadvisor.model.Portfolio;
import com.hsbc.roboadvisor.payload.DeviationRequest;
import com.hsbc.roboadvisor.payload.PortfolioRequest;
import com.hsbc.roboadvisor.repository.PortfolioRepository;

@Service
public class PortfolioRepositoryService
{
    @Autowired
    private PortfolioRepository portfolioRepository;

    public Portfolio findPreferenceByPortfolioId(Integer portfolioId) {
        return this.portfolioRepository.findByPortfolioId(portfolioId);
    }

    public Boolean preferenceExistsByPortfolioId(Integer portfolioId) {
        return this.portfolioRepository.existsByPortfolioId(portfolioId);
    }

    public Portfolio savePreference(Integer portfolioId, PortfolioRequest portfolioRequest) {
        Portfolio portfolio = new Portfolio(portfolioId, portfolioRequest.getDeviation(), portfolioRequest.getPortfolioType());
        return this.portfolioRepository.save(portfolio);
    }

    public Portfolio updateDeviationByPortfolioId(Integer portfolioId, DeviationRequest deviationRequest) {
        Portfolio portfolio = this.portfolioRepository.findByPortfolioId(portfolioId);
        portfolio.setDeviation(deviationRequest.getDeviation());
        return this.portfolioRepository.save(portfolio);
    }
}
