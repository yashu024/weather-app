package com.user.api.entitytest;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import com.user.api.entity.User;

public class UserTest {
	
	private User user;

	@Before
	public void setUp() throws Exception {

		user = new User();
		user.setUserName("user1");;
		user.setFullName("Test user 1");
		user.setEmail("user1@gmail.com");
		user.setPassword("xyz@1234");
		user.setProfilePicture("image.jpg");

	}

	@Test
	public void test() {
		new BeanTester().testBean(User.class);
	}

}
