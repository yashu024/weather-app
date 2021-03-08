package com.favourite.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.favourite.api.entity.Favourite;
import com.favourite.api.repository.FavouriteRepository;

@Service
public class FavouriteService {
	
	@Autowired
	private FavouriteRepository favRepo;
	
	
	public boolean addOrRemoveFavouriteCity(String userName, String cityName)
	{
		boolean status = false;
		int favouriteId = isFavouriteCityByUserNameExists(userName, cityName);
		
		// Remove favourite in case the city was added already
		if(favouriteId > 0)
		{
			Favourite favourite = favRepo.findById(favouriteId).orElse(null);
			if(favourite != null)
			{
				status = true;
				favRepo.delete(favourite);
			}
			
		}
		else
		{
			Favourite favourite = new Favourite(userName, cityName);
			favourite = favRepo.save(favourite);
			if(favourite != null)
			{
				status = true;
			}
			
		}
		return status;
	}
	
	public void deleteAllFavoritesByUserName(String userName)
	{
		List<Favourite> favourites = favRepo.findByUserName(userName);
		
		for(Favourite eachFav : favourites)
		{
			favRepo.deleteById(eachFav.getFavouriteId());
		}
		
	}
	
	public List<String> getAllFavouriteCitiesByUserName(String userName)
	{
		List<String> favCities = new ArrayList<String>();
		List<Favourite> favourites = favRepo.findByUserName(userName);
		
		for(Favourite eachFav: favourites)
		{
			favCities.add(eachFav.getCityName());
		}
		
		return favCities;
	}
	
	
	public int isFavouriteCityByUserNameExists(String userName, String cityName)
	{
		int favouriteId = -1;
		
		List<Favourite> favourites = favRepo.findByUserName(userName);
		
		for(Favourite eachFav : favourites)
		{
			if(cityName.equalsIgnoreCase(eachFav.getCityName())) 
			{
				favouriteId = eachFav.getFavouriteId();
				break;
			}
		}
		
		
		return favouriteId;
	}

}
