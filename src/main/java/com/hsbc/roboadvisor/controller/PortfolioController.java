package com.hsbc.roboadvisor.controller;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
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

import com.hsbc.roboadvisor.exception.MissingHeaderException;
import com.hsbc.roboadvisor.exception.ResourceNotFoundException;
import com.hsbc.roboadvisor.model.Portfolio;
import com.hsbc.roboadvisor.payload.PortfolioRequest;
import com.hsbc.roboadvisor.service.PortfolioRepositoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

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
        @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
        @ApiIgnore HttpServletRequest httpServletRequest) {

        String customerId = getCustomerIdOrFail(httpServletRequest);

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
        @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
        @Valid @RequestBody PortfolioRequest portfolioRequest,
        @ApiIgnore HttpServletRequest httpServletRequest) {

        String customerId = getCustomerIdOrFail(httpServletRequest);

        _logger.info("Request to create portfolio with id: {} for customer id: {}", portfolioId, customerId);

        Portfolio result = portfolioService.savePreference(portfolioId, portfolioRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/roboadvisor/{id}")
            .buildAndExpand(result.getPortfolioId()).toUri();

        return ResponseEntity.created(location).body(result);
    }

    private String getCustomerIdOrFail(HttpServletRequest httpServletRequest)
    {
        String customerId = httpServletRequest.getHeader("x-custid");
        if (customerId == null) {
            throw new MissingHeaderException("Missing x-custid header!");
        }
        return customerId;
    }

}
