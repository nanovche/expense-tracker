package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.domain.Transaction;
import com.pairlearning.expensetracker.exceptions.EtBadRequestException;
import com.pairlearning.expensetracker.exceptions.EtResourceNotFoundException;
import com.pairlearning.expensetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	TransactionRepository transactionRepository;
	
	@Override
	public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
		return transactionRepository.fetchAll(userId, categoryId);
	}

	@Override
	public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
			throws EtResourceNotFoundException {
		return transactionRepository.fetchById(userId, categoryId, transactionId);
	}

	@Override
	public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note,
			Long transactionDate) throws EtBadRequestException {
		Transaction transaction = new Transaction.
				TransactionBuilder(userId, categoryId, amount, note, transactionDate)
				.build();
		Integer transactionId = transactionRepository.create(transaction);
		return transactionRepository.fetchById(userId, categoryId, transactionId);
	}

	@Override
	public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
			throws EtBadRequestException {
		transactionRepository.update(userId, categoryId, transactionId, transaction);
	}

	@Override
	public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
			throws EtResourceNotFoundException {
		transactionRepository.removeById(userId, categoryId, transactionId);
	}
}
