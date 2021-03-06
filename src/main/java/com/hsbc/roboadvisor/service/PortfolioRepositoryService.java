package com.hsbc.roboadvisor.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsbc.roboadvisor.exception.BadRequestException;
import com.hsbc.roboadvisor.model.PortfolioPreference.Allocation;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.payload.DeviationRequest;
import com.hsbc.roboadvisor.payload.PortfolioPreferenceUpdateRequest;
import com.hsbc.roboadvisor.payload.PortfolioRequest;
import com.hsbc.roboadvisor.repository.PortfolioRepository;

@Service
public class PortfolioRepositoryService
{
    @Autowired
    private PortfolioRepository portfolioRepository;

    public PortfolioPreference findPreferenceByPortfolioId(String portfolioId) {
        return this.portfolioRepository.findByPortfolioId(portfolioId);
    }

    public Boolean preferenceExistsByPortfolioId(String portfolioId) {
        return this.portfolioRepository.existsByPortfolioId(portfolioId);
    }

    public PortfolioPreference savePreference(String portfolioId, PortfolioRequest portfolioRequest) {
        totalPercentIs100OrFail(portfolioRequest.getAllocations());

        PortfolioPreference portfolio = new PortfolioPreference(portfolioId, portfolioRequest.getDeviation(),
                portfolioRequest.getType(), portfolioRequest.getAllocations());

        return this.portfolioRepository.save(portfolio);
    }

    public PortfolioPreference updatePortfolioPreference(String portfolioId, PortfolioPreferenceUpdateRequest updateRequest) {
        totalPercentIs100OrFail(updateRequest.getAllocations());
        PortfolioPreference portfolio = this.portfolioRepository.findByPortfolioId(portfolioId);

        portfolio.setAllocations(updateRequest.getAllocations());
        portfolio.setDeviation(updateRequest.getDeviation());

        return this.portfolioRepository.save(portfolio);
    }

    public PortfolioPreference updateDeviationByPortfolioId(String portfolioId, DeviationRequest deviationRequest) {
        PortfolioPreference portfolio = this.portfolioRepository.findByPortfolioId(portfolioId);
        portfolio.setDeviation(deviationRequest.getDeviation());

        return this.portfolioRepository.save(portfolio);
    }

    public PortfolioPreference updateAllocationsByPortfolioId(String portfolioId, List<Allocation> allocationList) {
        totalPercentIs100OrFail(allocationList);

        PortfolioPreference portfolio = this.portfolioRepository.findByPortfolioId(portfolioId);
        portfolio.setAllocations(allocationList);

        return this.portfolioRepository.save(portfolio);
    }

    private void totalPercentIs100OrFail(List<Allocation> allocationList)
    {
        BigDecimal totalPercent = new BigDecimal(0);

        for (Allocation allocation : allocationList) {
            totalPercent = totalPercent.add(allocation.getPercentage());
        }

        if (totalPercent.compareTo(new BigDecimal(100)) != 0) {
            throw new BadRequestException("Total percentage must be 100%. Please check again.");
        }
    }
}
