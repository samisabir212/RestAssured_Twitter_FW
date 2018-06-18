package com.Twitter.statuses;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.CommonAPI.RestUtilities;
import com.Twitter.constants.EndPoints;
import com.Twitter.constants.Path;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Posts {

	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;
	String Tweet_id = "";


	
	
	@BeforeMethod
	public void setup() {
		/*
		 * this whole code will be inside resSpec
		 *the resposnse spec has to pass the query paramter for user id for security
		 *	using getRequestSpec storing into requesstSpec 
		 *then taking that request spec that continas the query param and base path 
		 *and storing it into responseSpec.. this is important because im creating the response object
		 *without a response object... how will i validate the response?? so create a response Spec
		 *pass this "responseSpec" --> then().spec(--->responseSpec<---) then extract().response()  all this will
		 *be stored inside the Response Class child object as 'response'
		 *
		 *then after all that use JSONpath to get inside the response and validate the key's values
		 *you can assert the data from an external source such as a feature file or Excel file 
		 *
		 *Everything is connected to your restUtilities
		 *
		 *Rest Utilities contains the following
		 *
		 *getRequestSpecification
		 *getResponseSpecification
		 *createQueryParamter 
		 *createQueryParam (overridden version 'MAP' as passed parameter)
		 *getResponse (which returns a given end point)
		 *getResponse (which allows you to pass the reqSpec and the request type) 
		 *getJsonPath (which allows you to get inside the json payload and validate the key value pairs)
		 *
		 */
		requestSpec = RestUtilities.getRequestSpecification(); //<--Getting all object methods from rest utilities class
		requestSpec.queryParam("user_id", "sami sabir");
		requestSpec.basePath(Path.STATUSES);
		responseSpec = RestUtilities.getResponseSpecification();
	}

	@Test
	public void postTweet() {
		Response response = given()
				.spec(RestUtilities.createQueryParam(requestSpec, "status", "Framework tweet"))
				.when()
					.post(EndPoints.STATUSES_TWEET_POST)
				.then()
					.spec(responseSpec)
				.extract()
				.response();

		JsonPath jspath = RestUtilities.getJsonPath(response);

		Tweet_id = jspath.get("id_str");

	}
	
	@Test
	public void postTweet_HashMap() {
		
		Map<String,String> postTweetMap = new HashMap<String,String>();
		
		postTweetMap.put("status", "using hashmap query param method");
		
		Response response = given()
				.spec(RestUtilities.createQueryParam(requestSpec,postTweetMap))
				.when()
					.post(EndPoints.STATUSES_TWEET_POST)
				.then()
					.spec(responseSpec)
				.extract()
				.response();

		JsonPath jspath = RestUtilities.getJsonPath(response);

		Tweet_id = jspath.get("id_str");
		

		
	}

	@Test(dependsOnMethods= {"postTweet"})
	public void read_tweet_verification_exists() {
		RestUtilities.setEndPoint(EndPoints.STATUSES_TWEET_READ_SHOW_TWEET);
		Response res = RestUtilities.getResponse(RestUtilities.createQueryParam(requestSpec, "id", Tweet_id), "get");
		String text = res.path("text");
		System.out.println("The tweet text is: " + text);

	}

	@Test(dependsOnMethods= {"read_tweet_verification_exists"})
	public void delete_tweet() {
		given().spec(RestUtilities.createQueryParam(requestSpec, "id", Tweet_id))
		.when()
		.post(EndPoints.STATUSES_TWEET_DESTROY)
		.then()
		.specification(responseSpec);

	}

}
