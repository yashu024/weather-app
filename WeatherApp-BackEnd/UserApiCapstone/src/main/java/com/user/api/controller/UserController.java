package com.user.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.api.entity.User;
import com.user.api.exception.UserAlreadyExistsException;
import com.user.api.service.UserService;
import com.user.api.util.JwtUtil;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private ResponseEntity<?> response;
	
	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@RequestBody User user) throws UserAlreadyExistsException
	{
		// check for existing user
		User entity = userService.findUserByUserName(user.getUserName());
		if(entity != null)
		{
			throw new UserAlreadyExistsException("User with username "+ user.getUserName()+" already exists!");
		}
		else
		{
			entity = userService.createUser(user);
			if(entity == null)
			{
				response = new ResponseEntity<String>("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else
			{
				response = new ResponseEntity<String>("User added successfully", HttpStatus.CREATED);
			}
			
		}
		
		return response;
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody User user) throws UserAlreadyExistsException
	{
		// initialize user session
		user.setSession("active");
		
		// update the user
		User entity = userService.updateUser(user);
		if(entity == null)
		{
			response = new ResponseEntity<String>("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else
		{
			response = new ResponseEntity<String>("User updated successfully", HttpStatus.OK);
		}
		
		return response;
	}
	
	@GetMapping("/account")
	public ResponseEntity<?> getUserDetails(HttpServletRequest request)
	{
		// fetch token
		String token = request.getHeader("Authorization").substring(7);
		// fetch username
		String userName = jwtUtil.extractUsername(token);
		
		User user = userService.findUserByUserName(userName);
		if(user == null)
		{
			response =new ResponseEntity<String>("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else
		{
			response = new ResponseEntity<User>(user, HttpStatus.OK);
		}
		return response;
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteuser(HttpServletRequest request)
	{
		// fetch token
		String token = request.getHeader("Authorization").substring(7);
		// fetch username
		String userName = jwtUtil.extractUsername(token);
		
		if(userService.deleteUser(userName))
		{
			response = new ResponseEntity<String>("User successfully deleted", HttpStatus.OK);
		}
		else
		{
			response = new ResponseEntity<String>("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}

}
