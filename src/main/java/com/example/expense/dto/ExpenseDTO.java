package com.example.expense.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExpenseDTO {
	private Long id;
	private Double amount;
	private String category;
	private LocalDate date;
	private String note;
}
