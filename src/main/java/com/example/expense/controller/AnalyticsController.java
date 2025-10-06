package com.example.expense.controller;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.expense.dao.UserRepository;
import com.example.expense.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
	private final ExpenseService expenseService;

	private final UserRepository userRepository;

	@GetMapping("/monthly")
	public ResponseEntity<?> monthly(@RequestParam(required = false) String month, Authentication auth) {
		YearMonth ym = (month == null) ? YearMonth.now() : YearMonth.parse(month);
		var user = userRepository.findByUsername(auth.getName()).orElse(null);
		double total = expenseService.monthlyTotal(user, ym);
		Map<String, Object> res = new HashMap<>();
		res.put("month", ym.toString());
		res.put("total", total);
		return ResponseEntity.ok(res);
	}

	@GetMapping("/category")
	public ResponseEntity<?> category(@RequestParam(required = false) String month, Authentication auth) {
		YearMonth ym = (month == null) ? YearMonth.now() : YearMonth.parse(month);
		var user = userRepository.findByUsername(auth.getName()).orElse(null);
		List<Object[]> breakdown = expenseService.categoryBreakdown(user, ym);
		return ResponseEntity.ok(breakdown);
	}
}