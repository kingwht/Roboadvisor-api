package com.hsbc.roboadvisor.controller;

import com.hsbc.roboadvisor.exception.BadRequestException;
import com.hsbc.roboadvisor.model.Common.CurrencyAmount;
import com.hsbc.roboadvisor.model.Portfolio.Currency;
import com.hsbc.roboadvisor.model.Portfolio.Holding;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;
import com.hsbc.roboadvisor.model.PortfolioPreference.Allocation;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;
import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioType;
import com.hsbc.roboadvisor.payload.PortfolioRequest;
import com.hsbc.roboadvisor.service.JpaJsonConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.testng.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class CreatePortfolioPreferenceTest extends PortfolioPreferenceControllerTest {

    private static final String PID1 = "ID1";
    private static final String PID2 = "ID2";
    private static final String CID1 = "cid1";
    private static final Integer deviation = 1;
    private static final PortfolioType type = PortfolioType.fund;
    private static final List<Allocation> allocations = new ArrayList<Allocation>();
    private static final List<Holding>  holdings = new ArrayList<>();
    private PortfolioRequest pr1 = new PortfolioRequest();
    private Portfolio p1 = new Portfolio();
    private PortfolioPreference pp1 = new PortfolioPreference();
    private Allocation alloc1 = new Allocation();
    private Holding holding = new Holding();

    private JpaJsonConverter jpaJsonConverter = new JpaJsonConverter();

    @BeforeClass
    public void setup() {
        super.setup();

        // We assume that the getPortfolioPreference method relies on the portfolioRepositoryService
        pr1.setDeviation(deviation);
        pr1.setType(type);
        pr1.setAllocations(allocations);
        List<Portfolio> portfolios = new ArrayList<>();
        portfolios.add(p1);

        when(portfolioRepositoryService.findPreferenceByPortfolioId(PID1)).thenReturn(pp1);
        when(portfolioRepositoryService.findPreferenceByPortfolioId(PID2)).thenReturn(null);
        when(portfolioRepositoryService.savePreference(eq(PID1), any(PortfolioRequest.class))).thenReturn(pp1);
        when(fundSystemRequestService.getPortfolios(CID1)).thenReturn(portfolios);

        pp1.setPortfolioId(PID1);
        pp1.setDeviation(deviation);
        pp1.setPortfolioType(type);
        pp1.setAllocations(allocations);

        holding.setBalance(new CurrencyAmount(new BigDecimal(10), Currency.CAD));
        holding.setFundId(1);
        holding.setUnits(4);
        holdings.add(holding);

        p1.setId(PID1);
        p1.setCustomerId(CID1);
        p1.setHoldings(holdings);
    }

    @Test
    public void missingHeader() {
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + PID1))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            if(e.getClass() != MissingRequestHeaderException.class)
                fail("Received unexpected exception: " + e.getMessage());
        }
    }


    @Test
    public void throwsIfPortfolioIdDoesNotExist(){
        when(portfolioRepositoryService.findPreferenceByPortfolioId(PID2)).thenReturn(null);
        allocations.clear();
        PortfolioRequest pr2 = new PortfolioRequest(deviation, type, allocations);

        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + PID2)
                    .header("x-custid", CID1)
                    .content(jpaJsonConverter.convertToDatabaseColumn(pr2))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e){
            fail("Recieved unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void throwsIfInvalidAllocation() {
        allocations.clear();
        alloc1.setFundId(2);
        alloc1.setPercentage(new BigDecimal(0.7));
        allocations.add(alloc1);
        pp1.setAllocations(allocations);

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + PID1))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            if(e.getClass() != BadRequestException.class)
                fail("Received unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void createsPortfolioPreference() {
        allocations.clear();
        alloc1.setFundId(1);
        alloc1.setPercentage(new BigDecimal(1));
        allocations.add(alloc1);
        pp1.setAllocations(allocations);


        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/roboadvisor/portfolio/" + PID1)
                    .header("x-custid", CID1)
                    .content(jpaJsonConverter.convertToDatabaseColumn(pr1))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}
