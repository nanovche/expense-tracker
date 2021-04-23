package com.pairlearning.expensetracker.repository;

import com.pairlearning.expensetracker.domain.Transaction;
import com.pairlearning.expensetracker.exceptions.EtBadRequestException;
import com.pairlearning.expensetracker.exceptions.EtResourceNotFoundException;

import java.util.List;

public interface TransactionRepository {
	List<Transaction> fetchAll(Integer userId, Integer categoryId);
	
	Transaction fetchById(Integer userId, Integer categoryId, Integer transactionId) throws
	EtResourceNotFoundException;
	
	Integer create(Transaction transaction) throws
	EtBadRequestException;
	
	void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws
	EtBadRequestException;
	
	void removeById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;
}
