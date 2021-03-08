package com.user.api.restAssuredTest;

import org.testng.annotations.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeClass;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

@SpringBootTest
public class UserControllerTest {
	
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
	}
	
	@Test(priority = 1)
	void createUserSuccessTest()
	{
		Response response =given().contentType("application/json")
								.body(user).when().post("http://localhost:8090/user/signup")
								.then().statusCode(201)
								.log().body().extract().response();
	
		assertEquals(response.asString(), "User added successfully");
		
		
	}
	
	@Test(priority = 2)
	void createDuplicateUserFailureTest()
	{
		Response response =given().contentType("application/json")
								.body(user).when().post("http://localhost:8090/user/signup")
								.then().statusCode(409)
								.log().body().extract().response();
		
		assertTrue(response.asString().contains("already exists"));
		
		
	}

	@Test(priority = 3)
	void updateUserSuccessTest()
	{
		HashMap<String, String> authRequest = new HashMap<String, String>();
		authRequest.put("userName", "Test123");
		authRequest.put("password", "pass123");
		// login user to fetch token
		Response response = given().contentType("application/json").body(authRequest)
							.post("http://localhost:8090/user/login").then()
							.statusCode(200).log().body().extract().response();
		
		tokenHeader = response.getHeader("Authorization");
		
		user.put("email", "test@gmail.com");
		response =given().header("Authorization", tokenHeader)
				.contentType("application/json")
								.body(user).when().put("http://localhost:8090/user/update")
								.then().statusCode(200)
								.log().body().extract().response();
		
		assertTrue(response.asString().contains("User updated successfully"));
		
		
	}
	
	@Test(priority = 4)
	void updateUserWithoutTokenFailureTest()
	{
		Response response = given().contentType("application/json")
			.body(user).when().put("http://localhost:8090/user/update")
			.then().statusCode(403).log().body().extract().response();
		
		assertTrue(response.asString().contains("Access Denied"));
	}
	
	@Test(priority = 5)
	void getUserDetailsSuccessTest()
	{
		Response response = given().header("Authorization", tokenHeader)
		.get("http://localhost:8090/user/account").then().statusCode(200)
		.log().body().extract().response();
		
		assertEquals(response.asString(),"{\"userName\":\"Test123\",\"fullName\":\"Rest assured Test User 1\",\"email\":\"test@gmail.com\",\"password\":\"pass123\",\"profilePicture\":\"pic1.jpg\",\"session\":\"active\"}");
		
	}
	
	@Test(priority = 6)
	void getUserDetailsWithoutTokenFailureTest()
	{
		Response response = given()
		.get("http://localhost:8090/user/account").then().statusCode(403)
		.log().body().extract().response();
		
		assertTrue(response.asString().contains("Access Denied"));
		
	}
	
	
	@Test(priority = 8)
	void deleteUserSuccessTest()
	{
		Response response = given().header("Authorization", tokenHeader)
						.when().delete("http://localhost:8090/user/delete")
						.then().statusCode(200).log().body().extract().response();
		
		assertEquals(response.asString(), "User successfully deleted");
		
	}
	
	
	@Test(priority = 7)
	void deleteUserWithoutTokenFailureTest()
	{
		Response response = given().delete("http://localhost:8090/user/delete")
			.then().statusCode(403).log().body().extract().response();
		
		assertTrue(response.asString().contains("Access Denied"));
		
	}

}
