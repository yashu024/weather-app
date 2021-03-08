package com.user.api.restAssuredTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.util.HashMap;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.response.Response;

@SpringBootTest
public class UserAuthenticationControllerTest {
	
	private HashMap<String, String> user;
	private HashMap<String, String> authRequest;
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
		
		authRequest = new HashMap<String, String>();
		authRequest.put("userName", "Test123");
		authRequest.put("password", "pass12");
	}
	
	@AfterClass
	void teardown() {
		
		// login user
		given().contentType("application/json").body(authRequest)
		.post("http://localhost:8090/user/login").then()
		.statusCode(200);
		
		// remove the user
		given().header("Authorization", tokenHeader)
		.when().delete("http://localhost:8090/user/delete")
		.then().statusCode(200);
	}
	
	@Test(priority = 1)
	void loginWithoutUserExistingFailureTest()
	{
		Response response = given().contentType("application/json").body(authRequest)
				.post("http://localhost:8090/user/login").then()
				.statusCode(401).log().body().extract().response();
		
		assertTrue(response.asString().contains("Inavalid username/password"));

	}
	
	@Test(priority = 2)
	void loginWithInvalidCredentialsFailureTest()
	{
		// create user
		given().contentType("application/json")
		.body(user).when().post("http://localhost:8090/user/signup")
		.then().statusCode(201);
		
		// login
		Response response = given().contentType("application/json").body(authRequest)
		.post("http://localhost:8090/user/login").then()
		.statusCode(401).log().body().extract().response();
		
		assertTrue(response.asString().contains("Inavalid username/password"));
		
		
	}
	
	@Test(priority = 3)
	void loginWithValidCredentialsSuccessTest()
	{
		authRequest.put("password", "pass123");
		
		Response response = given().contentType("application/json").body(authRequest)
				.post("http://localhost:8090/user/login").then()
				.statusCode(200).log().body().extract().response();
		
		assertEquals(response.asString(), "Successfully Logged In");

		tokenHeader = response.getHeader("Authorization");
	}
	
	@Test(priority = 4)
	void logoutWithoutTokenFailureTest()
	{
		Response response = given().when().get("http://localhost:8090/user/logout")
		.then().statusCode(403).log().body().extract().response();
		
		assertTrue(response.asString().contains("Access Denied"));
	}
	
	@Test(priority = 5)
	void logoutSuccessTest()
	{
		Response response = given().header("Authorization", tokenHeader)
							.when().get("http://localhost:8090/user/logout")
							.then().statusCode(200).log().body().extract().response();
		
		assertTrue(response.asString().contains("logged out"));
	}

}
