package com.api.consumerController;

import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weatherApi")
public class ApiConsumerController {
	@Autowired
	private RestTemplate restTemplate;
	
	/**************** constants ****************************************************/
	
	/*** Api key can be found after login into https://cricapi.com
	* 	 Limit: (60 Hits per minute)
	* 
	* 	Note: Use your own API key
	***/
	private static final String API_KEY = "d7bf673d120f0053a08c35ea64c6a8d6";
	
	private static final String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?appid=" + API_KEY + "&units=metric";
	
	private static final String WEATHER_ICON_URL = "http://openweathermap.org/img/wn/";
	
	private static final String CITY_PARAM = "&q=";
	
	/***********************************************************************************************************************/
	
	/** for fetching weather details of major cities for Dashboard
	**  
	*/
	private final String cityNameList = "Hyderabad Itanagar Dispur Patna Raipur Goa Gandhinagar Chandigarh Shimla Ranchi Bengaluru Thiruvananthapuram Bhopal Mumbai Imphal Shillong Aizwal Kohima Bhubaneswar Jaipur Gangtok Chennai Hyderabad Agartala Lucknow Dehradun Kolkata";
	private final String filter = "timezone clouds dt coord base wind";
	
	// for storing response objects
	private ResponseEntity<?> response;
	
	/** API method for getting weather by city from an external API:  http://openweathermap.org
	 * 
	 * @param cityName
	 * @return weather of the given city
	 */
	@GetMapping("/city/{cityName}")
	public ResponseEntity<?> getWeatherByCity(@PathVariable("cityName") String cityName)
	{
		try
		{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> weatherDetails = restTemplate.getForObject(WEATHER_BASE_URL+CITY_PARAM+cityName, HashMap.class);
		
		if(weatherDetails.get("cod").toString().equals("200"))
		{
			// remove unwanted data
			weatherDetails = filterData(weatherDetails);
			response = new ResponseEntity<HashMap<String, Object>>(weatherDetails, HttpStatus.OK);
		}
		else
		{
			response = new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			response = new ResponseEntity<String>("No city found", HttpStatus.NOT_FOUND);
			
		}
		
		return response;
		
	}
	
	/** API method for getting weather of major cities from an external API:  http://openweathermap.org
	 * 
	 * @param 
	 * @return a list of hashmap containing weather of the major cities with valid response
	 */
	
	@SuppressWarnings("unchecked")
	@GetMapping("/dashboard")
	public ResponseEntity<?> getAllCitiesForDashboard()
	{
		// get all cities for dashboard
		String cities[] = cityNameList.split(" ");
		// initialize map for storing weather of major cities
		HashMap<String, Object> weatherByCity = new HashMap<String, Object>();
		
		// populate the map from external API and filter unwanted data
		for(String eachCity : cities)
		{
			weatherByCity.put(eachCity ,filterData(restTemplate.getForObject(WEATHER_BASE_URL+CITY_PARAM+eachCity, HashMap.class)));
		}
		
		response = new ResponseEntity<HashMap<String, Object>>(weatherByCity, HttpStatus.OK);
		
		return response;
	}
	
	/*** method for filtering the data
	 * 
	 * @param weatherData
	 * @return filtered hashmap object
	 */
	
	@SuppressWarnings("unchecked")
	private HashMap<String, Object> filterData(HashMap<String, Object> weatherData)
	{
		for(String eachAttr:filter.split(" "))
		{
			weatherData.remove(eachAttr);
		}
		
		// add weather icon url
		List<Object> weatherDetails = (List<Object>) weatherData.get("weather");
		HashMap<String, Object> weather = (HashMap<String, Object>) weatherDetails.get(0);
		String iconName = weather.get("icon").toString();
		weather.put("icon", WEATHER_ICON_URL + iconName + "@2x.png");
		weatherData.put("icon", WEATHER_ICON_URL + iconName + "@2x.png");
		
		weatherData.put("weather", weather);
		
		return weatherData;
	}
	

}
