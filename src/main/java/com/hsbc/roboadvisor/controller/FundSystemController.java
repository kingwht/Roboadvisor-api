package com.hsbc.roboadvisor.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hsbc.roboadvisor.payload.TransactionRequest;
import com.hsbc.roboadvisor.payload.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;
import com.hsbc.roboadvisor.service.FundRequestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Fund System API")
@RestController
@RequestMapping("/roboadvisor/fundsystem")
public class FundSystemController
{
    private static final Logger _logger = LoggerFactory.getLogger(FundSystemController.class);

    @Autowired
    private FundRequestService fundRequestService;

    @ApiOperation(value = "Get List of Portfolio for a customer Id.")
    @GetMapping("/portfolios")
    public ResponseEntity<?> getPortfolios(
            @RequestHeader(value = "x-custid") String customerId) {
        _logger.info("Getting portfolios for customer id: {}", customerId);

        List<Portfolio> portfolioList = fundRequestService.getPortfolios(customerId);
        return new ResponseEntity<>(portfolioList, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all HSBC funds.")
    @GetMapping("/funds")
    public ResponseEntity<?> getFunds(
            @RequestHeader(value = "x-custid") String customerId) {
        _logger.info("Getting Funds for customer id: {}", customerId);

        List<Fund> funds = fundRequestService.getFunds(customerId);
        return new ResponseEntity<>(funds, HttpStatus.OK);
    }

    @ApiOperation(value = "Get details for a specific fund.")
    @GetMapping("/funds/{fundId}")
    public ResponseEntity<?> getFund(
            @RequestHeader(value = "x-custid") String customerId,
            @PathVariable Integer fundId) {
        _logger.info("Getting fund {} for customer id: {}", fundId, customerId);

        Fund fund = fundRequestService.getFund(customerId, fundId);
        return new ResponseEntity<>(fund, HttpStatus.OK);
    }

    @ApiOperation(value = "Execute the transaction.")
    @PostMapping("/transaction")
    public ResponseEntity<?> executeTransaction(
            @RequestHeader(value = "x-custid") String customerId,
            @RequestBody TransactionRequest transactionRequest) {
        _logger.info("Execute the transaction for customer id: {} with portfolio Id {}", customerId,
                transactionRequest.getPortfolioId());

        TransactionResponse transactionResponse = fundRequestService.executeTransaction(customerId, transactionRequest);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Get total value of assets for a customer Id.")
    @GetMapping("/funds/total")
    public ResponseEntity<?> getTotalAssetValue(
            @RequestHeader(value = "x-custid") String customerId) {
        _logger.info("Getting total value of assets for customer id: {}", customerId);

        BigDecimal totalAsset = new BigDecimal(0);

        Map<Integer, BigDecimal> fundsMap = new HashMap<>();
        List<Fund> funds = fundRequestService.getFunds(customerId);
        funds.forEach(fund -> {
            fundsMap.put(fund.getFundId(),fund.getPrice().getAmount());
        });

        Map<Integer, Integer> fundIdUnitMap = new HashMap<>();
        List<Portfolio> portfolios = fundRequestService.getPortfolios(customerId);
        portfolios.forEach(portfolio -> portfolio.getHoldings().forEach(holding -> {
            if (!fundIdUnitMap.containsKey(holding.getFundId())) {
                fundIdUnitMap.put(holding.getFundId(), holding.getUnits());
            }else {
                Integer oldAmount = fundIdUnitMap.get(holding.getFundId());
                fundIdUnitMap.put(holding.getFundId(), holding.getUnits() + oldAmount);
            }
        }));

        for (Integer key : fundIdUnitMap.keySet()) {
            BigDecimal currentValue = fundsMap.get(key);
            Integer units = fundIdUnitMap.get(key);
            totalAsset = totalAsset.add(currentValue.multiply(new BigDecimal(units)));
        }

        return new ResponseEntity<>(totalAsset, HttpStatus.OK);
    }

}
