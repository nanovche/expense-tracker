package com.pairlearning.expensetracker.domain;

public class Category {

	private Integer categoryId;
	private Integer userId;
	private String title;
	private String description;
	private Double totalExpense;

	private Category (CategoryBuilder categorybuilder) {
		this.categoryId = categorybuilder.categoryId;
		this.userId = categorybuilder.userId;
		this.title = categorybuilder.title;
		this.description = categorybuilder.description;
		this.totalExpense = categorybuilder.totalExpense;
	}

	public static class CategoryBuilder {

		private Integer categoryId;
		private Integer userId;
		private String title;
		private String description;
		private Double totalExpense;

		public CategoryBuilder(Integer categoryId, Integer userId, String title, String description, Double totalExpense) {
			this.categoryId = categoryId;
			this.userId = userId;
			this.title = title;
			this.description = description;
			this.totalExpense = totalExpense;
		}

		public Category build() {
			return new Category(this);
		}
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Double getTotalExpenses() {
		return totalExpense;
	}

}
