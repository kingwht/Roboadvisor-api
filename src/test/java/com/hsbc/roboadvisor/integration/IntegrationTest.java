// a test that mocks the webservice call but involves real database connectivity.
// a test that makes a real webservice call but uses mock database connectivity.
// a test that makes a real webservice call and involves a real database connection.

/**
package com.hsbc.roboadvisor.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.hsbc.roboadvisor.RoboAdvisorApplication;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.sql.*;

import com.hsbc.roboadvisor.repository.PortfolioRepository;
// I don't understand how spinning up the SQL database in our local environment
// allows the API to connect to it 
// I don't understand whether a database connection needs to be listening to
// proceed

// Connect to our database

@SpringBootTest(classes = RoboAdvisorApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestPropertySource(properties = "server.port=5000")
public class IntegrationTest {

	String custId = "axlypv0e55";

	private int port = 8080;
	TestRestTemplate restTemplate = new TestRestTemplate();



	private static DockerComposeContainer env = new DockerComposeContainer(
		(new File("./compose-test.yml"))).withExposedService("mysql",3306, Wait.forListeningPort());



	@Test 
	public void DoesNothing(){
		System.out.println("I do nothing");
	}

	@Test 

	public void testHome(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		System.out.println(port);

		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/7265124") , HttpMethod.GET,new HttpEntity<>(headers), String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		System.out.println(entity.getBody());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}



}


**/