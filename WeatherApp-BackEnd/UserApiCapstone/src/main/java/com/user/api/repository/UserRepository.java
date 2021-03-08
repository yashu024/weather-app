package com.user.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	public List<User> findUserByfullName(String fullName);

}
