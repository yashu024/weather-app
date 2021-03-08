package com.api.restAssuredTest;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.response.Response;

@SpringBootTest
public class ApiConsumerControllerTest {
	
	private HashMap<String, String> user;
	private String tokenHeader;
	
	@BeforeClass
	void setup()
	{
		System.out.println("Initializing setup");
		user = new HashMap<String, String>();
		user.put("userName", "Test123");
		user.put("fullName", "Rest assured Test User 1");
		user.put("password", "pass123");
		user.put("email", "test123@restassured.com");
		user.put("profilePicture", "pic1.jpg");
		
		// create user
		given().contentType("application/json")
		.body(user).when().post("http://localhost:8090/user/signup")
		.then().statusCode(201);
		
		// login user
		HashMap<String, String> authRequest = new HashMap<String, String>();
		authRequest.put("userName", "Test123");
		authRequest.put("password", "pass123");
		
		Response response = given().contentType("application/json").body(authRequest)
				.post("http://localhost:8090/user/login").then()
				.statusCode(200).log().body().extract().response();

		tokenHeader = response.getHeader("Authorization");
		
	}
	
	@AfterClass
	void teardown() {
		// remove the user
		given().header("Authorization", tokenHeader)
		.when().delete("http://localhost:8090/user/delete")
		.then().statusCode(200);
	}
	
	@Test(priority = 1)
	void getWeatherByCityNameSuccessTest()
	{
		Response response = given().header("Authorization", tokenHeader)
							.get("http://localhost:8080/weatherApi/city/delhi")
							.then().statusCode(200).log().body().extract().response();
		assertTrue(response.asString().contains("\"name\":\"Delhi\",\"cod\":200"));
				
	}
	
	@Test(priority = 2)
	void getWeatherByCityWithoutTokenFailureTest()
	{
		Response response = given().get("http://localhost:8080/weatherApi/city/delhi")
							.then().statusCode(403).log().body().extract().response();
		
		assertTrue(response.asString().contains("Access Denied"));
	}
	
	@Test(priority = 3)
	void getWeatherForDashBoardSuccessTest()
	{
		Response response = given().header("Authorization", tokenHeader)
				.get("http://localhost:8080/weatherApi/dashboard")
				.then().statusCode(200).log().body().extract().response();
		
		assertTrue(response.asString().contains("\"name\":\"Lucknow\",\"cod\":200"));
		assertTrue(response.asString().contains("\"name\":\"Shimla\",\"cod\":200"));
		assertTrue(response.asString().contains("\"name\":\"Bengaluru\",\"cod\":200"));
		assertTrue(response.asString().contains("\"name\":\"Chandigarh\",\"cod\":200"));
		assertTrue(response.asString().contains("\"name\":\"Kolkata\",\"cod\":200"));
		assertTrue(response.asString().contains("\"name\":\"Hyderabad\",\"cod\":200"));
		assertTrue(response.asString().contains("\"name\":\"Jaipur\",\"cod\":200"));
	}
	
	@Test(priority = 4)
	void getWeatherForDashBoardWithoutTokenFailiureTest()
	{
		Response response = given().get("http://localhost:8080/weatherApi/dashboard")
							.then().statusCode(403).log().body().extract().response();
		
		assertTrue(response.asString().contains("Access Denied"));
	}

}
