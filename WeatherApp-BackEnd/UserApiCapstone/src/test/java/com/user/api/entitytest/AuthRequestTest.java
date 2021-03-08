package com.user.api.entitytest;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import com.user.api.entity.AuthRequest;

public class AuthRequestTest {
	
	private AuthRequest auth;
	@Before
	public void setUp() throws Exception {

		auth = new AuthRequest();
		auth.setUserName("user1");
		auth.setPassword("xyz@1234");

	}

	@Test
	public void test() {
		new BeanTester().testBean(AuthRequest.class);
	}


}
