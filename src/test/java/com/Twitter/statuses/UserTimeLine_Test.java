package com.Twitter.statuses;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.CommonAPI.RestUtilities;
import com.Twitter.constants.EndPoints;
import com.Twitter.constants.Path;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserTimeLine_Test {
	
	
	 RequestSpecification reqSpec;
	 ResponseSpecification resSpec;
	
	@BeforeClass
	public void setUp() {
		/*
		 * this whole code will be inside resSpec
		 */
		reqSpec = RestUtilities.getRequestSpecification(); //<--Getting all object methods from rest utilities class
		reqSpec.queryParam("user_id", "sami sabir"); //appended
		reqSpec.basePath(Path.STATUSES);//then set base path!
		resSpec = RestUtilities.getResponseSpecification(); //now you have a responseSpec object to use in TC
			
	}
	
	
	
	
	
	@Test(enabled = false)
	public void 	readTweets()	{
		given()
		.spec(RestUtilities.createQueryParam(reqSpec, "count"	, "4")) //limiting the amount of tweets we want to output by 1 check document of time line on twitter.. this should only return one tweet
		.when()
		.get(EndPoints.STATUSES_USER_TIMELINE)
		.then()
		.spec(resSpec)
		.log().all()
		.body("user.screen_name", hasItem("samisabir22"));
		
		
	}
	
	@Test(enabled = false)
	public void readTweets2_RESPONSE() {
		RestUtilities.setEndPoint(EndPoints.STATUSES_USER_TIMELINE);
		Response res = RestUtilities.getResponse(RestUtilities
				.createQueryParam(reqSpec, "count", "4"), "get");
		ArrayList<String> screenNameList = res.path("user.screen_name");
		System.out.println(screenNameList);
		Assert.assertTrue(screenNameList.contains("samisabir22"));
	}
	
	@Test
	public void postTweet() {
		RestUtilities.setEndPoint(EndPoints.STATUSES_TWEET_POST);
		RestUtilities.getRequestSpecification();
		RestUtilities.createQueryParam(reqSpec, "status", "shaan").post();
		RestUtilities.getResponse(reqSpec, "post");
		
		
		
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
