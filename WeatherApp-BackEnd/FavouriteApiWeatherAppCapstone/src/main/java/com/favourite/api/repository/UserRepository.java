package com.favourite.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.favourite.api.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	public List<User> findUserByfullName(String fullName);

}
