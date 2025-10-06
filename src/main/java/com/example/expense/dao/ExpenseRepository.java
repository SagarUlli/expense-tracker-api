package com.example.expense.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.expense.entity.Expense;
import com.example.expense.entity.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findByUserOrderByDateDesc(User user);

	@Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date >= :start AND e.date <= :end")
	List<Expense> findByUserAndDateBetween(@Param("user") User user, @Param("start") LocalDate start,
			@Param("end") LocalDate end);
}