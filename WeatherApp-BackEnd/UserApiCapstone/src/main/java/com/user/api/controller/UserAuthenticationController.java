package com.user.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.api.entity.AuthRequest;
import com.user.api.exception.InvalidUserNamePasswordException;
import com.user.api.service.UserService;
import com.user.api.util.JwtUtil;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserAuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService userService;
	
	private ResponseEntity<?> response;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws InvalidUserNamePasswordException
	{
		try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new InvalidUserNamePasswordException("Inavalid username/password");
        }
		
		response = ResponseEntity.ok()
				.header("Authorization", "Bearer "+jwtUtil.generateToken(authRequest.getUserName()))
				.body("Successfully Logged In");
		
		// create user session
		userService.createUserSession(authRequest.getUserName());
		
		return response;
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request)
	{
		// extract token
		String token = request.getHeader("Authorization").substring(7);
		//System.out.println(token);
		
		// extract userName
		String userName = jwtUtil.extractUsername(token);
		//System.out.println(userName);
		
		// remove user session
		userService.removeUserSession(userName);
		
		return new ResponseEntity<String>("logged out", HttpStatus.OK);
	}
	
	

}
