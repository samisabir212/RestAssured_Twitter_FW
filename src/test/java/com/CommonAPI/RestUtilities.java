package com.CommonAPI;

import com.Twitter.constants.Auth;
import com.Twitter.constants.Path;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.lessThan;
import static io.restassured.RestAssured.given;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RestUtilities {
	/*
	 * the request and response builder are kept as utilities
	 * 
	 */
	
	public static String ENDPOINT; // global variable for endpoints || use it with setEndPoint method
	
	// request and response builders
	public static RequestSpecBuilder REQUEST_BUILDER; //to build a request
	public static ResponseSpecBuilder RESPONSE_BUILDER; //to build a response


	public static RequestSpecification REQUEST_SPEC; //object of request spec
	public static ResponseSpecification RESPONSE_SPEC;//object of response spec

	// create the reusable methods
	public static void setEndPoint(String epoint) {
		//setter to get the end point.
		//call the EndPoints class and pass a endpoint 
		ENDPOINT = epoint;

	}
	
	
	
//	
//	public static RequestSpecification getRquestWithOutAuth() {
//		
//	REQUEST_BUILDER = new RequestSpecBuilder();
//	REQUEST_BUILDER.setBasePath()
//		
//		return REQUEST_SPEC;
//
//	}
//	
//	

	// add to this request spec return type
	//call this in test class to get request specification 
	//this needs to return a request specification object
	/*
	 * basically this get request spec, is using Oauth 
	 * creating a request builder object
	 * provide the oauth data
	 * setting and passing the base uri
	 * setting the authscheme by passing the authSchecme object to .setAuth method
	 * finally building the request
	 * and returning the request into REQUST_SPEC object.
	 */
	public static RequestSpecification getRequestSpecification() {

		AuthenticationScheme authScheme = // check out the other authScheme methods
				RestAssured.oauth(Auth.CONSUMER_KEY, Auth.CONSUMER_SECRET, Auth.ACCESS_TOKEN, Auth.ACCESS_SECRET);		
		REQUEST_BUILDER = new RequestSpecBuilder(); //create new request builder object **** KEY object 
		REQUEST_BUILDER.setBaseUri(Path.BASE_URI); //setting base URI
		
		//the setAuth method sets your authorization
		REQUEST_BUILDER.setAuth(authScheme);  //setting authorization
		REQUEST_SPEC = REQUEST_BUILDER.build(); //building the request **Key finishing move
		return REQUEST_SPEC; // return the Request specification and stor into object
	}

	/*
	 * this method build you a template setup to get the 
	 * response with an expected code status of 200
	 * it also makes sure the expected response time is less than 3 seconds
	 * 
	 * Response specification
	 * i want this method to get the response
	 * validate that the response is 200 (ok)
	 * make sure the response time is less than 3 seconds
	 * finally build the response and pass it into a response_spec object
	 */
	public static ResponseSpecification getResponseSpecification() {
		RESPONSE_BUILDER = new ResponseSpecBuilder(); // create reponse builder object
		RESPONSE_BUILDER.expectStatusCode(200); // verify the expected status code is OK
		RESPONSE_BUILDER.expectResponseTime(lessThan(3l), TimeUnit.SECONDS); // verify the response time.. if more than 3 seconds then fail
		RESPONSE_SPEC = RESPONSE_BUILDER.build();
		return RESPONSE_SPEC;

	}

	/*
	 * this is a method to create paramter. query parameter for the end point target
	 */
	public static RequestSpecification createQueryParam(RequestSpecification rspec, String param, String value) {
		/*
		 * it needs to pass the request spec class reference object 
		 * in order to return the query paramter method rspec.queryParam(param,value)
		 */
		return rspec.queryParam(param, value);
	}
	
	
	//create query param using map key value pair
	public static RequestSpecification createQueryParam(RequestSpecification rspec, Map<String, String> queryMap) {

		return rspec.queryParams(queryMap);

	}

	

	/*
	 * get response returns the response of the endpoint
	 */
	public static Response getResponse() {
		return given().get(ENDPOINT);

	}

	
	
	/*
	 * this method allows you to get the response by 
	 * passing what type of crud type method being used.
	 * depending on the type you pass, then you will get
	 * a response of that type either get, post,put delete
	 * it then returns a log of the whole response to the output console.
	 * basically, give me the response depending on the response type
	 * 
	 */
	public static Response getResponse(RequestSpecification reqSpec, String type) {

		REQUEST_SPEC.spec(reqSpec);
		Response response = null;
		if (type.equalsIgnoreCase("get")) {
			response = given().spec(REQUEST_SPEC).get(ENDPOINT);
		} else if (type.equalsIgnoreCase("post")) {
			response = given().spec(REQUEST_SPEC).post(ENDPOINT);
		} else if (type.equalsIgnoreCase("put")) {
			response = given().spec(REQUEST_SPEC).put(ENDPOINT);
		} else if (type.equalsIgnoreCase("delete")) {
			response = given().spec(REQUEST_SPEC).delete(ENDPOINT);
		} else {
			System.out.println("type is not supported");
		}

		response.then().log().all();
		response.then().spec(RESPONSE_SPEC);
		return response;

	}

	/*
	 * 
	 */
	public static JsonPath getJsonPath(Response res) {
		String jsonpath = res.asString();
		return new JsonPath(jsonpath);

	}

	public static XmlPath getXmlPath(Response res) {
		String xmlpath = res.asString();
		return new XmlPath(xmlpath);

	}

	// reset the basebath
	public void resetBasePath() {
		RestAssured.basePath = null;
		//previous setting goes to null
	}

	public void setContentType(ContentType type) {
		given().contentType(type);
	}

}
