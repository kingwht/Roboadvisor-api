package com.hsbc.roboadvisor.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hsbc.roboadvisor.model.Recommendation.Recommendation;

@Repository
public interface RecommendationRepository extends CrudRepository<Recommendation, Integer>
{
    Recommendation findByPortfolioId(String portfolioId);

    Recommendation findByRecommendationId(Integer recommendationId);

    Boolean existsByPortfolioId(String portfolioId);

    Boolean existsByRecommendationId(Integer recommendationId);
}
