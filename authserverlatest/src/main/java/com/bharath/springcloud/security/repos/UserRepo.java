package com.bharath.springcloud.security.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.springcloud.security.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
