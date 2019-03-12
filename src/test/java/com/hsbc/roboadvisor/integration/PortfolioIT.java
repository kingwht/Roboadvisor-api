package com.hsbc.roboadvisor.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.hsbc.roboadvisor.model.PortfolioPreference.PortfolioPreference;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PortfolioIT extends AbstractTestNGSpringContextTests
{
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetPortfolio() {
        String customerId = "test_customer_id";
        String portfolioId = "1";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-custid", customerId);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String portfolioPreferenceURI = "/roboadvisor/portfolio/" + portfolioId;

        ResponseEntity<PortfolioPreference> response = this.restTemplate.exchange(portfolioPreferenceURI,
                HttpMethod.GET, entity,  new ParameterizedTypeReference<PortfolioPreference>(){});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        PortfolioPreference preference = response.getBody();
        assertThat(preference.getDeviation()).isEqualTo(0);
    }
}
