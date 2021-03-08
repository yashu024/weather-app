package com.user.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.user.api.entity.User;

public interface UserService {
	// for common user
	public User createUser(User user);
	public boolean deleteUser(String userName);
	public User updateUser(User user);
	
	// for admin
	public User findUserByUserName(String userName);
	public List<User> findAllUsers();
	public List<User> findUserByfullName(String fullName);
	
	// utility
	public void createUserSession(String userName);
	public void removeUserSession(String userName);
	public String getUserSession(String userName);

}
