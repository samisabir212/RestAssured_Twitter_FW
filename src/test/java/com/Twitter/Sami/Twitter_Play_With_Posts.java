package com.Twitter.Sami;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.CommonAPI.RestUtilities;
import com.Twitter.constants.EndPoints;
import com.Twitter.constants.Path;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Twitter_Play_With_Posts {

	public static RequestSpecification requestSpecification;
	ResponseSpecification responseSpecification;
	private String Tweet_id = "";
	private String User_id ="";

	@BeforeClass
	public void setup() {
		requestSpecification = RestUtilities.getRequestSpecification();
		requestSpecification.basePath(Path.STATUSES);

		responseSpecification = RestUtilities.getResponseSpecification();
		

	}

	@Test(enabled = true,priority = 1)
	public void post_MY_Tweet() {
		
		Response response = given()
			.spec(RestUtilities.createQueryParam(
					requestSpecification, "status", "fart"))
			.when()
				.post(EndPoints.STATUSES_TWEET_POST)
			.then()
				.extract()
				.response();
		
		JsonPath jsPath = RestUtilities.getJsonPath(response);
		Tweet_id = jsPath.getString("id_str");
		User_id = jsPath.getString("user.id_str");
		System.out.println(Tweet_id + " "+User_id );
		 
	}
	
	@Test(enabled = true,priority = 2)
	public void read_MY_Tweet() {
		RestUtilities.setEndPoint(EndPoints.STATUSES_TWEET_READ_SHOW_TWEET);
		Response response = RestUtilities.getResponse(
				RestUtilities.createQueryParam(requestSpecification, "id", Tweet_id), "get");
		String tweet_text = response.path("text");
		System.out.println("tweet text is :: " + tweet_text);
		Assert.assertTrue(tweet_text.contains("another tweet just to read"));
	}
	
	
	@Test(enabled = false)
	public void delete_Tweet_ByID() {
		
	given().spec(requestSpecification)
		
	}

}
