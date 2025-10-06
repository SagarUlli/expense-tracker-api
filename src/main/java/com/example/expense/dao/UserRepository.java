package com.example.expense.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.expense.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}