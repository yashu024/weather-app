package com.favourite.api.restAssured;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.response.Response;

@SpringBootTest
public class FavouriteControllerTest {
	
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
		
		HashMap<String, String> authRequest = new HashMap<String, String>();
		authRequest.put("userName", "Test123");
		authRequest.put("password", "pass123");
		
		// login user
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
	void addFavouriteCitiesSuccessTest()
	{
		// add Delhi
		Response response = given().header("Authorization", tokenHeader)
							.put("http://localhost:9000/favApi/toggle/delhi")
							.then().statusCode(200).log().body().extract().response();
		
		assertEquals(response.asString(), "Toggle success");
		
		// add Bangalore
		response = given().header("Authorization", tokenHeader)
				.put("http://localhost:9000/favApi/toggle/bangalore")
				.then().statusCode(200).log().body().extract().response();

		assertEquals(response.asString(), "Toggle success");
		
		// add Hyderabad
		response = given().header("Authorization", tokenHeader)
				.put("http://localhost:9000/favApi/toggle/hyderabad")
				.then().statusCode(200).log().body().extract().response();

		assertEquals(response.asString(), "Toggle success");
		
	}
	
	@Test(priority = 2)
	void addFavouriteCitiesWithoutTokenFailureTest()
	{
		// add Jaipur
		Response response = given().put("http://localhost:9000/favApi/toggle/jaipur")
							.then().statusCode(403).log().body().extract().response();
		
		assertEquals(response.asString(), "Access Denied");
	
	}
	
	@Test(priority = 3)
	void getAllCitiesByUserSuccessTest()
	{
		Response response = given().header("Authorization", tokenHeader)
				.get("http://localhost:9000/favApi/user/all")
				.then().statusCode(200).log().body().extract().response();

		assertTrue(response.asString().contains("delhi"));
		assertTrue(response.asString().contains("bangalore"));
		assertTrue(response.asString().contains("hyderabad"));
	}
	
	@Test(priority = 4)
	void getAllCitiesByUserWithoutTokenFailureTest()
	{
		Response response = given().get("http://localhost:9000/favApi/user/all")
				.then().statusCode(403).log().body().extract().response();

		assertEquals(response.asString(), "Access Denied");
	}
		
	
	@Test(priority = 5)
	void removeFavouriteCitiesSuccessTest()
	{
		// add Delhi
		Response response = given().header("Authorization", tokenHeader)
							.put("http://localhost:9000/favApi/toggle/delhi")
							.then().statusCode(200).log().body().extract().response();
		
		assertEquals(response.asString(), "Toggle success");
		
	}
	
	@Test(priority = 6)
	void removeAllFavoriteCitiesByUserWithoutTokenFailureTest()
	{
		Response response = given().delete("http://localhost:9000/favApi/delete/user")
							.then().statusCode(403).log().body().extract().response();

		assertTrue(response.asString().contains("Access Denied"));
	}
	
	@Test(priority = 7)
	void removeAllFavoriteCitiesByUserSuccessTest()
	{
		Response response = given().header("Authorization", tokenHeader)
				.delete("http://localhost:9000/favApi/delete/user")
				.then().statusCode(200).log().body().extract().response();

		assertTrue(response.asString().contains("Success"));
	}
	
	@Test(priority = 8)
	void getAllCitiesByUserFailureTest()
	{
		Response response = given().header("Authorization", tokenHeader)
				.get("http://localhost:9000/favApi/user/all")
				.then().statusCode(404).log().body().extract().response();

		assertTrue(response.asString().contains("No favourite cities found"));
	}

}
