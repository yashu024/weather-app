package com.favourite.api.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.favourite.api.service.FavouriteService;
import com.favourite.api.util.JwtUtil;

@RestController
@RequestMapping("/favApi")
public class FavouriteController {
	
	@Autowired
	private FavouriteService favService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private ResponseEntity<?> response;
	
	@GetMapping("/user/all")
	public ResponseEntity<?> getAllFavouriteCitiesByUser(HttpServletRequest request)
	{
		String token = request.getHeader("Authorization").substring(7);
		String userName = jwtUtil.extractUsername(token);
		
		List<String> favCityList = favService.getAllFavouriteCitiesByUserName(userName);
		
		if(favCityList.size() > 0)
		{
			HashMap<String, Object> favCities = new HashMap<String, Object>();
			favCities.put(userName, favCityList);
			response = new ResponseEntity<HashMap<String, Object>>(favCities, HttpStatus.OK);
		}
		else
		{
			response = new ResponseEntity<String>("No favourite cities found", HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	//@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping("/toggle/{cityName}")
	public ResponseEntity<?> toggleFavourite(@PathVariable("cityName") String cityName, HttpServletRequest request)
	{
		String token = request.getHeader("Authorization").substring(7);
		String userName = jwtUtil.extractUsername(token);
		System.out.println(userName+"\t"+cityName);
		if(favService.addOrRemoveFavouriteCity(userName, cityName))
		{
			response = new ResponseEntity<String>("{\n"
					+ "    \"message\": \"Toggle success\"\n"
					+ "}", HttpStatus.OK);
		}
		else
		{
			response = new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@DeleteMapping("/delete/user")
	public ResponseEntity<?> removeAllFavouritesByUser(HttpServletRequest request)
	{
		String token = request.getHeader("Authorization").substring(7);
		String userName = jwtUtil.extractUsername(token);
		
		favService.deleteAllFavoritesByUserName(userName);
		
		response = new ResponseEntity<String>("Success", HttpStatus.OK);
		
		return response;
	}

}
