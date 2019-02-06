package com.hsbc.roboadvisor.controller;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hsbc.roboadvisor.exception.BadRequestException;
import com.hsbc.roboadvisor.exception.ResourceNotFoundException;
import com.hsbc.roboadvisor.model.Allocation;
import com.hsbc.roboadvisor.model.Portfolio;
import com.hsbc.roboadvisor.model.PortfolioType;
import com.hsbc.roboadvisor.payload.AllocationsRequest;
import com.hsbc.roboadvisor.payload.DeviationRequest;
import com.hsbc.roboadvisor.payload.PortfolioRequest;
import com.hsbc.roboadvisor.service.PortfolioRepositoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Portfolio API")
@RestController
@RequestMapping("/roboadvisor/portfolio")
public class PortfolioController {

    private static final Logger _logger = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    private PortfolioRepositoryService portfolioService;

    @ApiOperation(value = "Get portfolio asset allocation and deviation preference.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved portfolio preference.", response = Portfolio.class),
            @ApiResponse(code = 400, message = "Invalid Portfolio ID."),
            @ApiResponse(code = 404, message = "No portfolio preference found.")
    })
    @GetMapping("/{portfolioId}")
    public ResponseEntity<?> getPortfolioPreference(
        @RequestHeader(value = "x-custid") Integer customerId,
        @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId) {

        _logger.info("Getting portfolio id: {} for customer id: {}", portfolioId, customerId);

        Portfolio portfolio = portfolioService.findPreferenceByPortfolioId(portfolioId);
        if (portfolio == null) {
            throw new ResourceNotFoundException("Portfolio", "PortfolioId", portfolioId);
        }
        return new ResponseEntity<>(portfolio, HttpStatus.OK);
    }


    @ApiOperation(value = "Create a portfolio asset allocation and deviation preference.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created portfolio asset allocation and deviation preferences."),
            @ApiResponse(code = 400, message = "Invalid post portfolio preference request.")
    })
    @PostMapping("/{portfolioId}")
    public ResponseEntity<?> createPortfolioPreference(
        @RequestHeader(value = "x-custid") Integer customerId,
        @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
        @Valid @RequestBody PortfolioRequest portfolioRequest) {

        _logger.info("Request to create portfolio with portfolio id: {} for customer id: {}", portfolioId, customerId);

        allocationListValidOrFail(portfolioRequest.getAllocations(), portfolioRequest.getPortfolioType());

        Portfolio result = portfolioService.savePreference(portfolioId, portfolioRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/roboadvisor/{id}")
            .buildAndExpand(result.getPortfolioId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Update a portfolio preference allocation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the portfolio's preferred allocations."),
            @ApiResponse(code = 404, message = "Invalid Portfolio ID.")
    })
    @PutMapping("/{portfolioId}/allocations")
    public ResponseEntity<?> setPortfolioAllocation(
            @RequestHeader(value = "x-custid") Integer customerId,
            @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
            @Valid @RequestBody AllocationsRequest allocationsRequest) {

        _logger.info("Request to update portfolio allocations with portfolio id: {} for customer id: {}", portfolioId, customerId);

        Portfolio portfolio = portfolioService.findPreferenceByPortfolioId(portfolioId);
        if (portfolio == null) {
            throw new ResourceNotFoundException("Portfolio", "PortfolioId", portfolioId);
        }

        allocationListValidOrFail(allocationsRequest.getAllocations(), portfolio.getPortfolioType());

        this.portfolioService.updateAllocationsByPortfolioId(portfolioId, allocationsRequest.getAllocations());
        return ResponseEntity.ok(allocationsRequest.getAllocations());
    }

    @ApiOperation(value = "Update a portfolio deviation percentage.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the portfolio's deviation."),
            @ApiResponse(code = 404, message = "Invalid Portfolio ID.")
    })
    @PutMapping("/{portfolioId}/deviation")
    public ResponseEntity<?> setPortfolioDeviation(
            @RequestHeader(value = "x-custid") Integer customerId,
        @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
        @Valid @RequestBody DeviationRequest deviationRequest) {

        _logger.info("Request to update portfolio deviation with portfolio id: {} for customer id: {}", portfolioId, customerId);

        Boolean portfolioExists = this.portfolioService.preferenceExistsByPortfolioId(portfolioId);
        if (!portfolioExists) {
            throw new ResourceNotFoundException("Portfolio", "PortfolioId", portfolioId);
        }

        Portfolio result = this.portfolioService.updateDeviationByPortfolioId(portfolioId, deviationRequest);
        Map<String, Integer> body = Collections.singletonMap("deviation", result.getDeviation());
        return ResponseEntity.ok(body);
    }

    private void allocationListValidOrFail(List<Allocation> allocationList, PortfolioType portfolioType)
    {
        for (Allocation allocation : allocationList) {
            if (portfolioType.equals(PortfolioType.category) && allocation.getFundId() != null) {
                throw new BadRequestException("Only one Category or Fund Id can be set. Please check again.");
            }else if (portfolioType.equals(PortfolioType.fund) && allocation.getCategory() != null){
                throw new BadRequestException("Only one Category or Fund Id can be set. Please check again.");
            }
            
            if (allocation.getCategory() == null && allocation.getFundId() == null) {
                throw new BadRequestException("Missing Category or Fund Id. Please check again.");
            } else if (allocation.getCategory() != null && allocation.getFundId() != null) {
                throw new BadRequestException("Only one Category or Fund Id can be set. Please check again.");
            }
        }
    }
}
