package com.pairlearning.expensetracker.domain;

public class Transaction {

	private Integer transactionId;
	private Integer categoryId;
	private Integer userId;
	private Double amount;
	private String note;
	private Long transactionDate;
	private Transaction(TransactionBuilder transactionBuilder) {
		this.transactionId = transactionBuilder.transactionId;
		this.categoryId = transactionBuilder.categoryId;
		this.userId = transactionBuilder.userId;
		this.amount = transactionBuilder.amount;
		this.note = transactionBuilder.note;
		this.transactionDate = transactionBuilder.transactionDate;
	}

	public static class TransactionBuilder {

		private Integer transactionId;
		private Integer categoryId;
		private Integer userId;
		private Double amount;
		private String note;
		private Long transactionDate;

		public TransactionBuilder(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) {
			this.categoryId = categoryId;
			this.userId = userId;
			this.amount = amount;
			this.note = note;
			this.transactionDate = transactionDate;
		}

		public TransactionBuilder setTransactionId(Integer transactionId) {
			this.transactionId = transactionId;
			return this;
		}

		public Transaction build(){
			return new Transaction(this);
		}
	}

	public Integer getTransactionId() {
		return transactionId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public Integer getUserId() {
		return userId;
	}
	public Double getAmount() {
		return amount;
	}
	public String getNote() {
		return note;
	}public Long getTransactionDate() {
		return transactionDate;
	}

}
