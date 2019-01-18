package com.hsbc.roboadvisor.controller;

import java.net.URI;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hsbc.roboadvisor.exception.ResourceNotFoundException;
import com.hsbc.roboadvisor.model.Portfolio;
import com.hsbc.roboadvisor.payload.PortfolioRequest;
import com.hsbc.roboadvisor.repository.PortfolioRepository;

@RestController
@RequestMapping("/roboadvisor")
public class PortfolioController {

    private static final Logger _logger = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    PortfolioRepository portfolioRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPortfolio(
        @PathVariable String id) {

        _logger.info("Get portfolio id: {}", id);
        try {
            Portfolio portfolio = portfolioRepository.findByPortfolioId(id);
            if (portfolio == null) {
                throw new ResourceNotFoundException("Portfolio", "PorfolioId", id);
            }
            return new ResponseEntity<>(portfolio, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    String.format("%s not found with %s : '%s'", e.getResourceName(), e.getFieldName(), e.getFieldValue()), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/{id}")
    public ResponseEntity<?> createPortfolio(
        @PathVariable int id,
        @Valid @RequestBody PortfolioRequest portfolioRequest) {
        
            _logger.info("Request to create portfolio with id: {}", id);
            if (portfolioRepository.existsByPortfolioId(id)) {
                return new ResponseEntity<>(String.format("Portfolio Id with %s already exists!", id), HttpStatus.BAD_REQUEST);
            }
            Portfolio portfolio = new Portfolio(id, portfolioRequest.getDeviation(), portfolioRequest.getPortfolioType());
            Portfolio result = portfolioRepository.save(portfolio);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/roboadvisor/{id}")
                .buildAndExpand(result.getPortfolioId()).toUri();
            return ResponseEntity.created(location).body(result);
    }

}
