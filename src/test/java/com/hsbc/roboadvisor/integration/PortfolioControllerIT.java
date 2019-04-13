// a test that mocks the webservice call but involves real database connectivity.
// a test that makes a real webservice call but uses mock database connectivity.
// a test that makes a real webservice call and involves a real database connection.

package com.hsbc.roboadvisor.integration;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.hsbc.roboadvisor.RoboAdvisorApplication;


@SpringBootTest(classes = RoboAdvisorApplication.class,webEnvironment = WebEnvironment.DEFINED_PORT)

public class PortfolioControllerIT
		extends AbstractTestNGSpringContextTests {

	private static final String custId = "axlypv0e55";
	private static final String porfolioIDInt = "9795213";
	@LocalServerPort
	private int              port;
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testCreateaPortfolios(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String portofolio1 = "{\n " +
				"  \"allocations\": [" +
				"    {\n" +
				"      \"fundId\": 23503,\n" +
				"      \"percentage\": 25\n" +
				"    },\n" +
				"    {\n" +
				"      \"fundId\": 23500,\n" +
				"      \"percentage\": 50\n" +
				"    },\n" +
				"    {\n" +
				"      \"fundId\": 23459,\n" +
				"      \"percentage\": 25\n" +
				"    }\n" +
				"  ],\n" +
				"  \"deviation\": 3,\n" +
				"  \"type\": \"fund\"\n" +
				"}";
		HttpEntity<String> entity = new HttpEntity<String>(portofolio1, headers);
		ResponseEntity<String> req = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213") , HttpMethod.POST,entity, String.class);
		Assert.assertEquals(req.getStatusCode(), HttpStatus.CREATED);
	}


	@Test
	public void testBadGetPortfolio(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795") , HttpMethod.GET,new HttpEntity<>(headers), String.class);
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.NOT_FOUND);
	}


	@Test(dependsOnMethods={"testCreateaPortfolios"})
	public void testGoodGetPortfolio() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		String portfolio1 = "{\n" +
				"  \"portfolioId\": \"9795213\",\n" +
				"  \"deviation\": 3,\n" +
				"  \"portfolioType\": \"fund\",\n" +
				"  \"allocations\": [\n" +
				"    {\n" +
				"      \"fundId\": 23503,\n" +
				"      \"percentage\": 25\n" +
				"    },\n" +
				"    {\n" +
				"      \"fundId\": 23500,\n" +
				"      \"percentage\": 50\n" +
				"    },\n" +
				"    {\n" +
				"      \"fundId\": 23459,\n" +
				"      \"percentage\": 25\n" +
				"    }\n" +
				"  ]\n" +
				"}";
		JSONObject jsonOb = new JSONObject(portfolio1);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213") , HttpMethod.GET,new HttpEntity<>(headers), String.class);
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.OK);
		System.out.println(entity.getBody());
		JSONAssert.assertEquals(jsonOb, new JSONObject(entity.getBody()), JSONCompareMode.STRICT);
	}

	@Test
	public void testPutBadAllocation(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String allocationChange = "[ { \"fundId\": 3, \"percentage\": 25 }]";
		HttpEntity<String> params = new HttpEntity<String>(allocationChange, headers);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/allocations") , HttpMethod.PUT,params, String.class);
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.BAD_REQUEST);
	}


	@Test(dependsOnMethods={"testGoodGetPortfolio"})
	public void testGoodPutAllocation(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-custid",custId);
		String allocationChange = "[{\"fundId\": 23503, \"percentage\": 25}, { \"fundId\": 23500, \"percentage\": 50 }, { \"fundId\": 23459, \"percentage\": 25}]";
		HttpEntity<String> params = new HttpEntity<String>(allocationChange, headers);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/allocations") , HttpMethod.PUT,params, String.class);
		System.out.println(entity.getBody());
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.OK);
	}

	@Test(dependsOnMethods={"testGoodPutAllocation"})
	public void testAllocationPersisitedtoPortfolio() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		String portfolio1 = "{\n" +
				"  \"portfolioId\": \"9795213\",\n" +
				"  \"deviation\": 3,\n" +
				"  \"portfolioType\": \"fund\",\n" +
				"  \"allocations\": [\n" +
				"    {\n" +
				"      \"fundId\": 23503,\n" +
				"      \"percentage\": 25\n" +
				"    },\n" +
				"    {\n" +
				"      \"fundId\": 23500,\n" +
				"      \"percentage\": 50\n" +
				"    },\n" +
				"    {\n" +
				"      \"fundId\": 23459,\n" +
				"      \"percentage\": 25\n" +
				"    }\n" +
				"  ]\n" +
				"}";
		JSONObject jsonOb = new JSONObject(portfolio1);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213") , HttpMethod.GET,new HttpEntity<>(headers), String.class);
		JSONAssert.assertEquals(jsonOb, new JSONObject(entity.getBody()), JSONCompareMode.STRICT);
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.OK);
	}

	@Test(dependsOnMethods={"testAllocationPersisitedtoPortfolio"})
	public void testPutDeviation(){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-custid",custId);
		String deviationChange = "{\n" +
				"  \"deviation\": 4\n" +
				"}";
		HttpEntity<String> params = new HttpEntity<String>(deviationChange, headers);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/deviation") , HttpMethod.PUT,params, String.class);
		System.out.println(entity.getBody());
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.OK);
	}

	@Test(dependsOnMethods={"testPutDeviation"})
	public void testPutDeviationPersisitedtoPortfolio() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		String portfolio1 = "{\n" +
				"  \"portfolioId\": \"9795213\",\n" +
				"  \"deviation\": 4,\n" +
				"  \"portfolioType\": \"fund\",\n" +
				"  \"allocations\": [\n" +
				"    {\n" +
				"      \"fundId\": 23503,\n" +
				"      \"percentage\": 25\n" +
				"    },\n" +
				"    {\n" +
				"      \"fundId\": 23500,\n" +
				"      \"percentage\": 50\n" +
				"    },\n" +
				"    {\n" +
				"      \"fundId\": 23459,\n" +
				"      \"percentage\": 25\n" +
				"    }\n" +
				"  ]\n" +
				"}";
		JSONObject jsonOb = new JSONObject(portfolio1);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213") , HttpMethod.GET,new HttpEntity<>(headers), String.class);
		System.out.println(entity.getBody());
		JSONAssert.assertEquals(jsonOb, new JSONObject(entity.getBody()), JSONCompareMode.STRICT);
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.OK);
	}


	@Test(dependsOnMethods={"testPutDeviationPersisitedtoPortfolio"}, enabled = false)
	public void testGetCurrentPortfolio(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/rebalance") , HttpMethod.POST,new HttpEntity<>(headers), String.class);
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.OK);
		System.out.println(entity.getBody());
		System.out.println(entity.getBody());
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);

	}


	@Test(dependsOnMethods={"testPutDeviationPersisitedtoPortfolio"})
	public void testPOSTRecommendationAndExecute() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid", custId);

		JSONObject beforePortfolioStates = getPortfolioStats(porfolioIDInt);
		Assert.assertTrue(beforePortfolioStates.names() != null);
		HashMap<Integer, JSONObject> holdingsBefore = hashJSONarrayWithPortID((JSONArray) beforePortfolioStates.opt("holdings"));

		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/rebalance"), HttpMethod.POST, new HttpEntity<>(headers), String.class);
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);

		JSONArray transactions = pullOutRecommendationTransacations(entity);
		int recommendID = pullOutRecommendationID(entity);
		Assert.assertNotEquals(recommendID, 0);

		if (transactions.length() == 0) {
			NewPutAllocation(headers);
			entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/rebalance"), HttpMethod.POST, new HttpEntity<>(headers), String.class);
			Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
			recommendID = pullOutRecommendationID(entity);
			transactions = pullOutRecommendationTransacations(entity);
		}
		entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/recommendation/" + String.valueOf(recommendID) + "/execute"), HttpMethod.POST, new HttpEntity<>(headers), String.class);
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
	}
	
	@Test(dependsOnMethods={"testPOSTRecommendationAndExecute"})
	public void testmodifyRecommendation() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-custid", custId);
		String recommendation = "[{\"action\":\"buy\",\"fundId\": 23500,\"units\":321},{\"action\":\"sell\",\"fundId\":23503,\"units\":114}]";
		HttpEntity<String> params = new HttpEntity<String>(recommendation, headers);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/recommendation/1/modify"), HttpMethod.PUT, params, String.class);
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
		JSONArray arrayActual = new JSONObject(entity.getBody()).optJSONArray("transactions");
		JSONArray arrayExpected = new JSONArray(recommendation);
		JSONAssert.assertEquals(arrayActual, arrayExpected, JSONCompareMode.STRICT);
	}

	@Test(dependsOnMethods={"testmodifyRecommendation"})
	public void testInvalidPortfolioandCateogryMix() throws JSONException{
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String portofolio = "{\n " +
				"  \"allocations\": [" +
				"    {\n" +
				"      \"fundId\": 23456,\n" +
				"      \"percentage\": 25\n" +
				"    },\n" +
				"    {\n" +
				"      \"category\": 1,\n" +
				"      \"percentage\": 50\n" +
				"    }\n" +
				"  ],\n" +
				"  \"deviation\": 3,\n" +
				"  \"type\": \"category\"\n" +
				"}";
		HttpEntity<String> entity = new HttpEntity<String>(portofolio, headers);
		ResponseEntity<String> req = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213") , HttpMethod.POST,entity, String.class);
		Assert.assertEquals("Only one portfolio type of Category or Fund Id can be set. Please check again.", new JSONObject(req.getBody()).optString("message"));
		Assert.assertEquals(req.getStatusCode(), HttpStatus.BAD_REQUEST);
	}



	@Test(dependsOnMethods={"testInvalidPortfolioandCateogryMix"})
	public void testInValidPortfolioswithDuplicateCategories() throws JSONException{
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String portofolio1 = "{\n " +
				"  \"allocations\": [" +
				"    {\n" +
				"      \"category\": 1,\n" +
				"      \"percentage\": 20\n" +
				"    },\n" +
				"    {\n" +
				"      \"category\": 2,\n" +
				"      \"percentage\": 40\n" +
				"    },\n" +
				"    {\n" +
				"      \"category\": 2,\n" +
				"      \"percentage\": 40\n" +
				"    }\n" +
				"  ],\n" +
				"  \"deviation\": 3,\n" +
				"  \"type\": \"category\"\n" +
				"}";
		HttpEntity<String> entity = new HttpEntity<String>(portofolio1, headers);
		ResponseEntity<String> req = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213") , HttpMethod.POST,entity, String.class);
		Assert.assertEquals("Non-unique allocation entries. Please check again.", new JSONObject(req.getBody()).optString("message"));
		Assert.assertEquals(req.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test(dependsOnMethods={"testInValidPortfolioswithDuplicateCategories"})
	public void testValidPortfolioswithCategories() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String portofolio = "{\n " +
				"  \"allocations\": [" +
				"    {\n" +
				"      \"category\": 3,\n" +
				"      \"percentage\": 40\n" +
				"    },\n" +
				"    {\n" +
				"      \"category\": 2,\n" +
				"      \"percentage\": 60\n" +
				"    }\n" +
				"  ],\n" +
				"  \"deviation\": 5,\n" +
				"  \"type\": \"category\"\n" +
				"}";
		HttpEntity<String> entity = new HttpEntity<String>(portofolio, headers);
		ResponseEntity<String> req = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213") , HttpMethod.POST,entity, String.class);
		Assert.assertEquals(req.getStatusCode(), HttpStatus.CREATED);
	}

	@Test(dependsOnMethods={"testInValidPortfolioswithDuplicateCategories"})
	public void testValidPortfolioswithInvalidCategory() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String portofolio = "{\n " +
				"  \"allocations\": [" +
				"    {\n" +
				"      \"category\": 1,\n" +
				"      \"percentage\": 40\n" +
				"    },\n" +
				"    {\n" +
				"      \"category\": 2,\n" +
				"      \"percentage\": 60\n" +
				"    }\n" +
				"  ],\n" +
				"  \"deviation\": 5,\n" +
				"  \"type\": \"category\"\n" +
				"}";
		HttpEntity<String> entity = new HttpEntity<String>(portofolio, headers);
		ResponseEntity<String> req = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213") , HttpMethod.POST,entity, String.class);
		Assert.assertEquals(req.getStatusCode(), HttpStatus.BAD_REQUEST);
		String message = new JSONObject(req.getBody()).optString("message");
		Assert.assertEquals("Cannot use a category that does not exist in a portfolio. Please try again.", message);
	}


	@Test(dependsOnMethods={"testValidPortfolioswithCategories"})
	public void testPOSTRecommendationForCatogory() throws JSONException{
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid", custId);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/rebalance"), HttpMethod.POST, new HttpEntity<>(headers), String.class);
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
		JSONObject body = new JSONObject(entity.getBody());
		JSONArray recommendationList  = body.optJSONArray("recommendedTransactionList");
		System.out.println(entity.getBody());
		Assert.assertNotEquals(entity.getBody(),null);
		Assert.assertNotEquals(recommendationList,null);
	}



	@Test(dependsOnMethods={"testValidPortfolioswithCategories"})
	public void testRecommendationPreferencesRanked() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-custid", custId);
		String budget = "{\"budget\":10000,\"fundCategory\":2}";
		String rankedItems = "[{\"action\":\"buy\",\"fundId\": 23500,\"units\":47},{\"action\":\"buy\",\"fundId\":23458,\"units\":79},{\"action\":\"buy\",\"fundId\":23459,\"units\":42}]";
		HttpEntity<String> params = new HttpEntity<String>(budget,headers);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("roboadvisor/portfolio/rebalance/ranking"), HttpMethod.POST, params, String.class);
		JSONArray arrayActual = new JSONArray(entity.getBody());
		JSONArray arrayExpected = new JSONArray(rankedItems);
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
		JSONAssert.assertEquals(arrayActual, arrayExpected, JSONCompareMode.STRICT);
	}

	@Test(dependsOnMethods={"testRecommendationPreferencesRanked"})
	public void testRecommendationPreferencesInvalid() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-custid", custId);
		String budget = "{\"budget\":10000,\"fundCategory\":1}";
		HttpEntity<String> params = new HttpEntity<String>(budget,headers);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("roboadvisor/portfolio/rebalance/ranking"), HttpMethod.POST, params, String.class);
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
	}



	// Helper Methods

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	private JSONObject getPortfolioStats(String portfolioID)  {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-custid",custId);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/fundsystem/portfolios") , HttpMethod.GET,new HttpEntity<>(headers), String.class);
		Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
		try {
			JSONArray allportfolios = new JSONArray(entity.getBody());
			for (int i = 0; i < allportfolios.length(); i++){
				String pid = (String) allportfolios.getJSONObject(i).get("id");
				if (portfolioID.equals(pid) ){
					return allportfolios.getJSONObject(i);
				}
			}
		} catch (JSONException e){
			System.out.println("Error parsing JSON body from resposne");
			return new JSONObject();
		}
		System.out.print("Portfolio ID does not exist ");
		return new JSONObject();
	}

	private JSONArray pullOutRecommendationTransacations(ResponseEntity<String> entity) {
		JSONObject recommendations = new JSONObject();
		JSONArray transactaions = new JSONArray();
		try {
			recommendations = new JSONObject(entity.getBody());
		} catch(JSONException e){
			Assert.fail("Invalid JSONobject returned by rebalance Put");
			Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
		}
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
		try {
			transactaions = (JSONArray) recommendations.get("transactions");
		} catch(JSONException e){
			Assert.fail("Invalid return JSON object, No transaction field");
		}
		return transactaions;
	}

	private int pullOutRecommendationID(ResponseEntity<String> entity) {
		JSONObject recommendations = new JSONObject();
		int recommendID = 0;
		try {
			recommendations = new JSONObject(entity.getBody());
		} catch(JSONException e){
			Assert.fail("Invalid JSONobject returned by rebalance Put");
			Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
		}
		Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
		try {
			recommendID = recommendations.getInt("recommendationId");
		} catch(JSONException e){
			Assert.fail("Invalid return JSON object, No recommendationId field");
		}
		return recommendID;
	}

	private double getFundIdPrice(HttpHeaders headers, String fundID) {
		double fundPrice = 0;
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> params = new HttpEntity<String>(headers);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/fundsystem/funds/" + fundID) , HttpMethod.GET,params, String.class);
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.OK);
		try {
			 fundPrice = new JSONObject(entity.getBody()).optJSONObject("price").optInt("amount");
			return fundPrice;
		} catch (JSONException e) {
			Assert.fail("Invalid JSONobject returned by rebalance Put");
		}
		return fundPrice;
	}



	private void NewPutAllocation(HttpHeaders headers) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		String allocationChange = "[ { \"fundId\": 23503, \"percentage\": 0}, { \"fundId\": 23500, \"percentage\": 98 }, { \"fundId\": 23459, \"percentage\": 2}]";
		HttpEntity<String> params = new HttpEntity<String>(allocationChange, headers);
		ResponseEntity<String> entity = this.restTemplate.exchange(createURLWithPort("/roboadvisor/portfolio/9795213/allocations") , HttpMethod.PUT,params, String.class);
		Assert.assertEquals(entity.getStatusCode(),HttpStatus.OK);
	}

	private HashMap hashJSONarrayWithPortID(JSONArray array) {
		HashMap<Integer,JSONObject> hashPortID = new HashMap<Integer,JSONObject>();
		for (int i  = 0; i < array.length();i++){
			JSONObject value = array.optJSONObject(i);
			hashPortID.put((Integer) value.opt("fundId"), value);
		}
		return hashPortID;
	}
}


