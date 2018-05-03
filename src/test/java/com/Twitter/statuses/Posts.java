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
				.spec(RestUtilities.createQueryParam(requestSpec,postTweetMap.get("status")))
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
