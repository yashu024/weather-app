package com.user.api.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	private String userName;
	private String fullName;
	private String email;
	private String password;
	private String profilePicture;
	
	private String session = "inactive";
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String userName, String fullName, String password, String email, String profilePicture) {
		super();
		this.userName = userName;
		this.fullName = fullName;
		this.password = password;
		this.email = email;
		this.session = "inactive";
		this.profilePicture = profilePicture;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	
	@Override
	public String toString() {
		return "User [userName=" + userName + ", fullName=" + fullName + ", email=" + email + ", password=" + password
				+ ", profilePicture=" + profilePicture + ", session=" + session + "]";
	}
	
	
	
	
	
	
	
	
}