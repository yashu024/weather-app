package com.user.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.user.api.entity.User;
import com.user.api.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		return userRepo.save(user);
	}

	@Override
	public boolean deleteUser(String userName) {
		// TODO Auto-generated method stub
		boolean response = false;
		User user = findUserByUserName(userName);
		if(user != null)
		{
			userRepo.delete(user);
			response =  true;
		}
		return response;
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return userRepo.save(user);
	}

	@Override
	public User findUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return userRepo.findById(userName).orElse(null);
	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll();
	}

	@Override
	public List<User> findUserByfullName(String fullName) {
		// TODO Auto-generated method stub
		return userRepo.findUserByfullName(fullName);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = findUserByUserName(username);
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());
	}

	@Override
	public void createUserSession(String userName) {
		// TODO Auto-generated method stub
		User user = findUserByUserName(userName);
		user.setSession("active");
		userRepo.save(user);
	}

	@Override
	public void removeUserSession(String userName) {
		// TODO Auto-generated method stub
		User user = findUserByUserName(userName);
		user.setSession("inactive");
		userRepo.save(user);
		
	}

	@Override
	public String getUserSession(String userName) {
		// TODO Auto-generated method stub
		User user = findUserByUserName(userName);
		return user.getSession();
	}
	
	

}
