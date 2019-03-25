package com.hsbc.roboadvisor.controller;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
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
import com.hsbc.roboadvisor.exception.EmptyTransactionException;
import com.hsbc.roboadvisor.exception.ResourceNotFoundException;
import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Portfolio.Holding;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;
import com.hsbc.roboadvisor.model.PortfolioPreference.Allocation;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioType;
import com.hsbc.roboadvisor.model.Recommendation.Recommendation;
import com.hsbc.roboadvisor.model.Recommendation.Transaction;
import com.hsbc.roboadvisor.payload.DeviationRequest;
import com.hsbc.roboadvisor.payload.FundRecommendationRequest;
import com.hsbc.roboadvisor.payload.PortfolioRequest;
import com.hsbc.roboadvisor.payload.TransactionRequest;
import com.hsbc.roboadvisor.payload.TransactionResponse;
import com.hsbc.roboadvisor.service.FundRecommendationService;
import com.hsbc.roboadvisor.service.FundSystemRequestService;
import com.hsbc.roboadvisor.service.PortfolioRepositoryService;
import com.hsbc.roboadvisor.service.RecommendationRepositoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Portfolio Preference API")
@RestController
@RequestMapping("/roboadvisor/portfolio")
public class PortfolioController {

    private static final Logger _logger = LoggerFactory.getLogger(PortfolioController.class);

    private PortfolioRepositoryService portfolioService;

    private RecommendationRepositoryService recommendationRepositoryService;

    private FundSystemRequestService fundSystemRequestService;

    private FundRecommendationService fundRecommendationService;

    @Autowired
    public PortfolioController(
            PortfolioRepositoryService portfolioService,
            RecommendationRepositoryService recommendationRepositoryService,
            FundSystemRequestService fundSystemRequestService,
            FundRecommendationService fundRecommendationService) {
        this.portfolioService = portfolioService;
        this.recommendationRepositoryService = recommendationRepositoryService;
        this.fundSystemRequestService = fundSystemRequestService;
        this.fundRecommendationService = fundRecommendationService;
    }

    @ApiOperation(value = "Get portfolio asset allocation and deviation preference.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved portfolio preference.", response = PortfolioPreference.class),
            @ApiResponse(code = 400, message = "Invalid Portfolio ID."),
            @ApiResponse(code = 404, message = "No portfolio preference found.")
    })
    @GetMapping("/{portfolioId}")
    public ResponseEntity<?> getPortfolioPreference(
        @RequestHeader(value = "x-custid") String customerId,
        @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId) {

        _logger.info("Getting portfolio id: {} for customer id: {}", portfolioId, customerId);

        if (customerId == null || customerId.isEmpty()) {
            throw new BadRequestException(
                    String.format("customerId cannot be null or empty.  Given: %s", customerId)
            );
        }

        if (portfolioId == null) {
            throw new BadRequestException("portfolioId cannot be null.");
        }

        PortfolioPreference portfolio = portfolioService.findPreferenceByPortfolioId(portfolioId);
        if (portfolio == null) {
            throw new ResourceNotFoundException("Portfolio Preference", "PortfolioId", portfolioId);
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
        @RequestHeader(value = "x-custid") String customerId,
        @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
        @Valid @RequestBody PortfolioRequest portfolioRequest) {

        _logger.info("Request to create portfolio with portfolio id: {} for customer id: {}", portfolioId, customerId);

        allocationListValidOrFail(portfolioRequest.getAllocations(), portfolioRequest.getType());
        checkAllValidFundsInPortfolio(portfolioRequest.getAllocations(), customerId, portfolioId);

        PortfolioPreference result = portfolioService.savePreference(portfolioId, portfolioRequest);

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
            @RequestHeader(value = "x-custid") String customerId,
            @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
            @Valid @RequestBody  List<Allocation> allocationList) {

        _logger.info("Request to update portfolio allocations with portfolio id: {} for customer id: {}", portfolioId, customerId);

        PortfolioPreference portfolio = portfolioService.findPreferenceByPortfolioId(portfolioId);
        if (portfolio == null) {
            throw new ResourceNotFoundException("Portfolio Preference", "PortfolioId", portfolioId);
        }

        allocationListValidOrFail(allocationList, portfolio.getPortfolioType());

        checkAllValidFundsInPortfolio(allocationList, customerId, portfolioId);

        this.portfolioService.updateAllocationsByPortfolioId(portfolioId, allocationList);
        return ResponseEntity.ok(allocationList);
    }

    private void allocationListValidOrFail(List<Allocation> allocationList, PortfolioType portfolioType) {
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

    // Checks if all funds in a given portfolio preference are part of the customer's portfolio
    private void checkAllValidFundsInPortfolio(List<Allocation> allocationsList, String customerId, Integer portfolioId) {
        List<Portfolio> customerPortfolioList = fundRequestService.getPortfolios(customerId);
        List<Holding> portfolioFunds = customerPortfolioOrFail(customerPortfolioList,portfolioId).getHoldings();

        Map<Integer, Boolean> mapPortfolioFunds = portfolioFundstoMap(portfolioFunds);
        for (Allocation allocation : allocationsList) {
            if (mapPortfolioFunds.get(allocation.getFundId()) == null) {
                throw new BadRequestException("Cannot use a fundID that does not exist in a portfolio. Please try again.");
            }
        }
    }

    @ApiOperation(value = "Update a portfolio deviation percentage.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the portfolio's deviation."),
            @ApiResponse(code = 404, message = "Invalid Portfolio ID.")
    })
    @PutMapping("/{portfolioId}/deviation")
    public ResponseEntity<?> setPortfolioDeviation(
            @RequestHeader(value = "x-custid") String customerId,
            @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
            @Valid @RequestBody DeviationRequest deviationRequest) {

        _logger.info("Request to update portfolio deviation with portfolio id: {} for customer id: {}", portfolioId, customerId);

        Boolean portfolioExists = this.portfolioService.preferenceExistsByPortfolioId(portfolioId);
        if (!portfolioExists) {
            throw new ResourceNotFoundException("Portfolio Preference", "PortfolioId", portfolioId);
        }

        PortfolioPreference result = this.portfolioService.updateDeviationByPortfolioId(portfolioId, deviationRequest);
        Map<String, Integer> body = Collections.singletonMap("deviation", result.getDeviation());
        return ResponseEntity.ok(body);
    }

    @ApiOperation(value = "Create a rebalance recommendation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created rebalance recommendation."),
            @ApiResponse(code = 400, message = "Invalid post recommendation preference request.")
    })
    @PostMapping("/{portfolioId}/rebalance")
    public ResponseEntity<?> createRecommendation(
            @RequestHeader(value = "x-custid") String customerId,
            @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId){

        _logger.info("Request to create recommendation for portfolio id: {} for customer id: {}", portfolioId, customerId);

        if (portfolioId == null) {
            throw new BadRequestException("portfolioId cannot be null.");
        }

        PortfolioPreference portfolioPreference = portfolioService.findPreferenceByPortfolioId(portfolioId);
        if (portfolioPreference == null) {
            throw new ResourceNotFoundException("Portfolio Preference", "PortfolioId", portfolioId);
        }

        List<Portfolio> customerPortfolioList = fundSystemRequestService.getPortfolios(customerId);
        List<Fund> customerFundList = fundSystemRequestService.getFunds(customerId);

        Portfolio portfolio = customerPortfolioOrFail(customerPortfolioList, portfolioId);

        Recommendation recommendation = this.recommendationRepositoryService.saveRecommendation(portfolio,
                customerFundList,portfolioPreference);

        return ResponseEntity.ok(recommendation);
    }

    private Portfolio customerPortfolioOrFail(List<Portfolio> customerPortfolioList, Integer portfolioId) {
        for (Portfolio portfolio : customerPortfolioList) {
            if (portfolio.getId().equals(portfolioId)) {
                return portfolio;
            }
        }
        throw new ResourceNotFoundException("Portfolio", "Portfolio Id", portfolioId);
    }

    @ApiOperation(value = "Create a list of suggested transactions for the given category and budget.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned ranked list of recommended funds to purchase.")
    })
    @PostMapping("/rebalance/ranking")
    public ResponseEntity<?> createCategoricalRanking(
            @RequestHeader(value = "x-custid") String customerId,
            @Valid @RequestBody FundRecommendationRequest fundRecommendationRequest) {

        if (customerId == null || customerId.isEmpty()) {
            throw new BadRequestException(
                    String.format("customerId cannot be null or empty.  Given: %s", customerId)
            );
        }

        List<Fund> customerFundList = fundSystemRequestService.getFunds(customerId);

        // Returned list of transactions is in DESC order with highest score (most recommended) fund as first entry.
        List<Transaction> transactionList = fundRecommendationService.getRecommendedTransactions(customerFundList,
                fundRecommendationRequest.getFundCategory(), fundRecommendationRequest.getBudget());

        return ResponseEntity.ok(transactionList);
    }

    @ApiOperation(value = "Execute the rebalance recommendation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully executed the rebalance recommendation.")
    })
    @PostMapping("/{portfolioId}/recommendation/{recommendationId}/execute")
    public ResponseEntity<?> executeRecommendation(
            @RequestHeader(value = "x-custid") String customerId,
            @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
            @ApiParam(value = "Recommendation ID", required = true) @PathVariable Integer recommendationId) {

        _logger.info("Request to execute recommendation for recommendation id: {} for customer id: {}", recommendationId, customerId);

        PortfolioPreference portfolioPreference = portfolioService.findPreferenceByPortfolioId(portfolioId);
        if (portfolioPreference == null) {
            throw new ResourceNotFoundException("Portfolio Preference", "PortfolioId", portfolioId);
        }

        Recommendation recommendation = recommendationRepositoryService.findRecommendationByRecommendationId(recommendationId);
        if (recommendation == null) {
            throw new ResourceNotFoundException("Recommendation", "Recommendation Id", recommendationId);
        }

        if (recommendation.getTransactions().size() == 0) {
            throw new EmptyTransactionException("Recommendation transaction cannot be empty.");
        }

        try{
            _logger.info("Execute the recommendation: {} for customer id: {}", recommendationId, customerId);
            // Create the transaction request
            TransactionRequest transactionRequest = new TransactionRequest(portfolioId, recommendation.getTransactions());

            // Execute Transaction
            TransactionResponse transactionResponse = fundSystemRequestService
                    .executeTransaction(customerId, transactionRequest);
            return ResponseEntity.ok(transactionResponse);
        }catch (Exception e) {
            throw new BadRequestException("The transaction fails");
        }
    }

    @ApiOperation(value = "Update the transactions in the rebalance recommendation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully executed the rebalance recommendation.")
    })
    @PutMapping("/{portfolioId}/recommendation/{recommendationId}/modify")
    public ResponseEntity<?> modifyRecommendation (
            @RequestHeader(value = "x-custid") String customerId,
            @ApiParam(value = "Portfolio ID", required = true) @PathVariable Integer portfolioId,
            @ApiParam(value = "Recommendation ID", required = true) @PathVariable Integer recommendationId,
            @Valid @RequestBody List<Transaction> transactionList) {

        _logger.info("Request to update recommendation for recommendation id: {} for customer id: {}", recommendationId, customerId);

        PortfolioPreference portfolioPreference = portfolioService.findPreferenceByPortfolioId(portfolioId);
        if (portfolioPreference == null) {
            throw new ResourceNotFoundException("Portfolio Preference", "PortfolioId", portfolioId);
        }

        Recommendation recommendation = recommendationRepositoryService.findRecommendationByRecommendationId(recommendationId);
        if (recommendation == null) {
            throw new ResourceNotFoundException("Recommendation", "Recommendation Id", recommendationId);
        }

       checkAllValidFundsInRecommendation(transactionList, customerId, portfolioId);


        Recommendation result = this.recommendationRepositoryService.updateRecommendationTransactions(
                recommendation, transactionList);

        return ResponseEntity.ok(result);
    }

    private Map<Integer, Boolean> portfolioFundstoMap(List<Holding> portfolioFunds){
        Map<Integer, Boolean> mapPortfolioFunds = new HashMap<>();
        for (Holding fund : portfolioFunds) {
            mapPortfolioFunds.put(fund.getFundId(), true);
        }
        return mapPortfolioFunds;
}


   private void checkAllValidFundsInRecommendation(List<Transaction> transactions, String customerId, Integer portfolioID) {
       List<Portfolio> customerPortfolioList = fundRequestService.getPortfolios(customerId);
       List<Holding> portfolioFunds = customerPortfolioOrFail(customerPortfolioList,portfolioID).getHoldings();

       Map<Integer, Boolean> mapPortfolioFunds = portfolioFundstoMap(portfolioFunds);
       for (Transaction transaction : transactions) {
           if (mapPortfolioFunds.get(transaction.getFundId()) == null) {
               throw new BadRequestException("Cannot use a fundID that does not exist in a portfolio. Please try again.");
           }
       }
   }


}
