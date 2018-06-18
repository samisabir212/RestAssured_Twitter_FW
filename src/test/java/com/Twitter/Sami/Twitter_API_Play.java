package com.Twitter.Sami;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.CommonAPI.RestUtilities;
import com.Twitter.constants.EndPoints;
import com.Twitter.constants.Path;
import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Twitter_API_Play {
	// all through http protocol
	// using CRUD operations
	// get read post delete update

	RequestSpecification requestSpecification;
	ResponseSpecification responseSpecification;

	Matchers matcher = new Matchers(); // hamcrest matcher

	@BeforeMethod
	public void setup() {

		requestSpecification = RestUtilities.getRequestSpecification();
		requestSpecification.queryParam("user_id", "sami sabir");// define the user you are using
		requestSpecification.basePath(Path.STATUSES); // then set base path!
		responseSpecification = RestUtilities.getResponseSpecification(); // now you have a responseSpec object to use
																			// in TC

	}

	@SuppressWarnings("static-access")
	@Test(enabled = false)
	public void readtweet() {

		given().spec(requestSpecification).when().get(EndPoints.STATUSES_USER_TIMELINE) // reading tweet from time line
																						// not read show tweet
				.then().log().all().spec(responseSpecification).body("user.name", matcher.hasItem("sami sabir"));

	}

	@SuppressWarnings("static-access")
	@Test(enabled = false)
	public void readTweet_withQuery_Param_limit_Count_Test() {
		/*
		 * query param will return a specific amount of tweets
		 */
		given().spec(RestUtilities.createQueryParam(requestSpecification, "count", "2")).when()
				.get(EndPoints.STATUSES_USER_TIMELINE) // reading tweet from time line not read show tweet
				.then().log() // logs output to console
				.body().spec(responseSpecification).body("user.name", matcher.hasItem("sami sabir"));

	}

	// short cut to readTweet_withQuery_Param_limit_Count_Test() method
	// make sure you set the endpoint
	// THIS THE WAY YOU SHOULD DO IT NOT THE METHOD ABOVE.
	@Test(enabled = false)
	public void verifyResponseTweetExists() {
		RestUtilities.setEndPoint(EndPoints.STATUSES_USER_TIMELINE);
		Response response = RestUtilities
				.getResponse(RestUtilities.createQueryParam(requestSpecification, "count", "3"), "get");
		List<String> screenNameList = response.path("user.name");
		for (String s : screenNameList) {
			System.out.println(s);
		}
		Assert.assertTrue(screenNameList.contains("sami sabir"));

	}

	@Test(enabled = true)
	public void read_Tweet_Text() {
		RestUtilities.setEndPoint(EndPoints.STATUSES_USER_TIMELINE);
		Response response = RestUtilities.getResponse(RestUtilities.createQueryParam(requestSpecification, "text",
				"creating tweet"), "get");
		System.out.println(response.asString());
				
		ArrayList<String> text_Of_A_Tweet = response.path("text");
		
		System.out.println("reading text of a tweet. ::"+text_Of_A_Tweet);

		for (String t : text_Of_A_Tweet) {
			System.out.println(t.indexOf(0));
		}

	}

	@Test(enabled = false)
	public void post_Tweet1() {

		Response response = given()
				.spec(RestUtilities.createQueryParam(requestSpecification, "status", "creating tweet"))
				.when()
				.post(EndPoints.STATUSES_TWEET_POST)
				.then()
				.spec(responseSpecification)
				.extract()
				.response();

		System.out.println(response.jsonPath());

	}
	
	
	

}
