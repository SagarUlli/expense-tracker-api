package com.example.expense.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expense.config.JWTUtil;
import com.example.expense.dto.AuthRequest;
import com.example.expense.dto.AuthResponse;
import com.example.expense.entity.User;
import com.example.expense.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	private final JWTUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody AuthRequest req) {
		if (userService.findByUsername(req.getUsername()).isPresent()) {
			return ResponseEntity.badRequest().body("Username already exists");
		}
		User u = userService.register(req.getUsername(), req.getPassword());
		String token = jwtUtil.generateToken(u.getUsername());
		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest req) {
		return userService.findByUsername(req.getUsername()).map(u -> {
			if (userService.checkPassword(req.getPassword(), u.getPassword())) {
				return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(u.getUsername())));
			} else
				return ResponseEntity.status(401).body("Invalid credentials");
		}).orElse(ResponseEntity.status(404).body("User not found"));
	}
}