package com.hsbc.roboadvisor.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hsbc.roboadvisor.model.Recommendation.Recommendation;

@Repository
public interface RecommendationRepository extends CrudRepository<Recommendation, Integer>
{
    Recommendation findByPortfolioId(Integer portfolioId);

    Recommendation findByRecommendationId(Integer recommendationId);

    Boolean existsByPortfolioId(Integer portfolioId);

    Boolean existsByRecommendationId(Integer recommendationId);
}
