package com.user.api.servicetest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.user.api.entity.User;
import com.user.api.repository.UserRepository;
import com.user.api.service.UserService;
import com.user.api.service.UserServiceImpl;

@SpringBootTest
public class UserServiceTest {
	
	@MockBean
	private UserRepository userRepository;
	
	@Autowired
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	public void testAddUser() {
		User user = new User("testuser", "Test User", "test123", "user@test.com", "xyz.jpg");
		when(userRepository.save(user)).thenReturn(user);
		assertEquals(user, userService.createUser(user));
		verify(userRepository, times(1)).save(user);
	}
	
	@Test
	public void testAddUserFailure() throws Exception {
		User user = new User("testuser", "Test User", "test123", "user@test.com", "xyz.jpg");
		when(userRepository.save(user)).thenReturn(null);
		assertEquals(null, userService.createUser(user));
		verify(userRepository, times(1)).save(user);
	}
	
	@Test
	public void testGetUserById() throws EntityNotFoundException{
		User user = new User("testuser", "Test User", "test123", "user@test.com", "xyz.jpg");
		when(userRepository.findById("testuser")).thenReturn(Optional.of(user));
	    assertThat(userService.findUserByUserName("testuser")).isEqualTo(user);
	}
	
	@Test
	public void testGetAllUsers(){
		
		User user1 = new User("testuser1", "Test User1", "test123", "user1@test.com", "xyz.jpg");
		User user2 = new User("testuser2", "Test User2", "test123", "user2@test.com", "abc.jpg");
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		
		when(userRepository.findAll()).thenReturn(userList);
		
		assertThat(userService.findAllUsers()).isEqualTo(userList);
	}
	
	@Test
	public void testDeleteUser(){
		User user = new User("testuser", "Test User", "test123", "user@test.com", "xyz.jpg");
		when(userRepository.findById("testuser")).thenReturn(Optional.of(user));
		when(userRepository.existsById(user.getUserName())).thenReturn(false);
	    assertFalse(userRepository.existsById(user.getUserName()));
	}
	
	@Test
	public void testUpdateUser(){
		User user = new User("testuser", "Test User", "test123", "user@test.com", "xyz.jpg");
		when(userRepository.findById("testuser")).thenReturn(Optional.of(user));
		
		user.setEmail("new_user@gmail.com");
		when(userRepository.save(user)).thenReturn(user);
		
		assertEquals("new_user@gmail.com", user.getEmail());;
		
	}

}
