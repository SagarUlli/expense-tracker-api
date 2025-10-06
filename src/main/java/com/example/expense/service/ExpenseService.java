package com.example.expense.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.expense.dao.ExpenseRepository;
import com.example.expense.dto.ExpenseDTO;
import com.example.expense.entity.Expense;
import com.example.expense.entity.User;

@Service
public class ExpenseService {
	@Autowired
	private ExpenseRepository expenseRepository;

	public Expense createExpense(User user, ExpenseDTO dto) {
		Expense e = new Expense();
		e.setUser(user);
		e.setAmount(dto.getAmount());
		e.setCategory(dto.getCategory());
		e.setDate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
		e.setNote(dto.getNote());
		return expenseRepository.save(e);
	}

	public List<Expense> listExpenses(User user) {
		return expenseRepository.findByUserOrderByDateDesc(user);
	}

	public Expense updateExpense(User user, Long id, ExpenseDTO dto) {
		return expenseRepository.findById(id).map(e -> {
			if (!e.getUser().getId().equals(user.getId()))
				return null;
			e.setAmount(dto.getAmount());
			e.setCategory(dto.getCategory());
			e.setDate(dto.getDate());
			e.setNote(dto.getNote());
			return expenseRepository.save(e);
		}).orElse(null);
	}

	public boolean deleteExpense(User user, Long id) {
		return expenseRepository.findById(id).map(e -> {
			if (!e.getUser().getId().equals(user.getId()))
				return false;
			expenseRepository.delete(e);
			return true;
		}).orElse(false);
	}

	public double monthlyTotal(User user, YearMonth month) {
		LocalDate start = month.atDay(1);
		LocalDate end = month.atEndOfMonth();
		return expenseRepository.findByUserAndDateBetween(user, start, end).stream().mapToDouble(Expense::getAmount)
				.sum();
	}

	public List<Object[]> categoryBreakdown(User user, YearMonth month) {
		LocalDate start = month.atDay(1);
		LocalDate end = month.atEndOfMonth();
		List<Expense> list = expenseRepository.findByUserAndDateBetween(user, start, end);
		return list.stream()
				.collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)))
				.entrySet().stream().map(e -> new Object[] { e.getKey(), e.getValue() }).collect(Collectors.toList());
	}
}