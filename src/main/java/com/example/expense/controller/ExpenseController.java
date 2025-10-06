package com.example.expense.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expense.dao.UserRepository;
import com.example.expense.dto.ExpenseDTO;
import com.example.expense.entity.Expense;
import com.example.expense.entity.User;
import com.example.expense.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
	private final ExpenseService expenseService;

	private final UserRepository userRepository;

	private User currentUser(Authentication auth) {
		return userRepository.findByUsername(auth.getName()).orElse(null);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody ExpenseDTO dto, Authentication auth) {
		User u = currentUser(auth);
		Expense e = expenseService.createExpense(u, dto);
		return ResponseEntity.ok(e);
	}

	@GetMapping
	public ResponseEntity<?> list(Authentication auth) {
		User u = currentUser(auth);
		List<Expense> list = expenseService.listExpenses(u);
		return ResponseEntity.ok(list);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ExpenseDTO dto, Authentication auth) {
		User u = currentUser(auth);
		Expense updated = expenseService.updateExpense(u, id, dto);
		if (updated == null)
			return ResponseEntity.status(404).body("Not found or unauthorized");
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id, Authentication auth) {
		User u = currentUser(auth);
		boolean ok = expenseService.deleteExpense(u, id);
		if (!ok)
			return ResponseEntity.status(404).body("Not found or unauthorized");
		return ResponseEntity.ok().build();
	}
}