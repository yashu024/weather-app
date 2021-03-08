package com.api.model;

import java.util.HashMap;


public class Weather {

	private String city;
	private HashMap<String,Object> weatherDetails =new HashMap<>();
	public Weather(String city, HashMap<String, Object> weatherDetails) {
		super();
		this.city = city;
		this.weatherDetails = weatherDetails;
	}

	
}
