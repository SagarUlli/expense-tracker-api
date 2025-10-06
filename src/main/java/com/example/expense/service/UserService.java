package com.example.expense.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.expense.dao.UserRepository;
import com.example.expense.entity.User;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public User register(String username, String rawPassword) {
		User u = new User();
		u.setUsername(username);
		u.setPassword(passwordEncoder.encode(rawPassword));
		u.setRole("ROLE_USER");
		return userRepository.save(u);
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean checkPassword(String rawPassword, String encoded) {
		return passwordEncoder.matches(rawPassword, encoded);
	}
}