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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hsbc.roboadvisor.model.Portfolio;

@Service
public class RequestService
{

    private static final Logger _logger = LoggerFactory.getLogger(RequestService.class);

    private RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }

    public List<Portfolio> getPortfolios(String custId)
    {
        _logger.info("Retrieving portfolio for {}.", custId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-custid", custId);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String url = "https://us-central1-useful-memory-229303.cloudfunctions.net/portfolios"; //TODO: Update this to our own service?
        ResponseEntity<List<Portfolio>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Portfolio>>(){});

        return responseEntity.getBody();
    }

}
