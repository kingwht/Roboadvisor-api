package com.hsbc.roboadvisor.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hsbc.roboadvisor.exception.ResourceNotFoundException;
import com.hsbc.roboadvisor.model.Fund.Fund;
import com.hsbc.roboadvisor.model.Portfolio.Portfolio;

@Service
public class FundRequestService
{

    private static final Logger _logger = LoggerFactory.getLogger(FundRequestService.class);

    private RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter textHtmlConverter = new MappingJackson2HttpMessageConverter();
        textHtmlConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        messageConverters.add(textHtmlConverter);

        MappingJackson2HttpMessageConverter ApplicationJson = new MappingJackson2HttpMessageConverter();
        ApplicationJson.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(ApplicationJson);

        restTemplate.setMessageConverters(messageConverters);
    }

    public List<Portfolio> getPortfolios(String customerId) {
        _logger.info("Retrieving portfolio for {}.", customerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-custid", customerId);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = "https://us-central1-useful-memory-229303.cloudfunctions.net/portfolios2";
        ResponseEntity<List<Portfolio>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Portfolio>>(){});

        if (responseEntity.getStatusCode() != HttpStatus.OK ) {
            throw new ResourceNotFoundException("Portfolio", "Customer Id", customerId);
        }

        return responseEntity.getBody();
    }

    public List<Fund> getFunds(String customerId) {
        _logger.info("Retrieving Funds customer: {}.", customerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-custid", customerId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = "https://us-central1-useful-memory-229303.cloudfunctions.net/funds2/";
        ResponseEntity<List<Fund>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Fund>>(){});

        if (responseEntity.getStatusCode() != HttpStatus.OK ) {
            throw new ResourceNotFoundException("Funds", "Customer Id", customerId);
        }

        return responseEntity.getBody();
    }

    public Fund getFund(String customerId, Integer fundId) {
        _logger.info("Retrieving Fund {} for customer: {}.", fundId, customerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-custid", customerId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = "https://us-central1-useful-memory-229303.cloudfunctions.net/fund2/" + fundId;
        ResponseEntity<Fund> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<Fund>(){});

        if (responseEntity.getStatusCode() != HttpStatus.OK ) {
            throw new ResourceNotFoundException("Fund", "Fund Id", fundId);
        }

        return responseEntity.getBody();
    }

}
