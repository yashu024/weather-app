package com.favourite.api.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favourite {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int favouriteId;
	private String userName;
	private String cityName;
	public Favourite() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Favourite(String userName, String cityName) {
		super();
		this.userName = userName;
		this.cityName = cityName;
	}
	public int getFavouriteId() {
		return favouriteId;
	}
	public void setFavouriteId(int favouriteId) {
		this.favouriteId = favouriteId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@Override
	public String toString() {
		return "Favourite [favouriteId=" + favouriteId + ", userName=" + userName + ", cityName=" + cityName + "]";
	}
	
	

}
