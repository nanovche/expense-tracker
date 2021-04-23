package com.pairlearning.expensetracker.controllers;

import com.pairlearning.expensetracker.domain.Transaction;
import com.pairlearning.expensetracker.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pairlearning.expensetracker.Constants.*;

@RestController
@RequestMapping("/api/categories/{categoryId}/transactions")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;

	@GetMapping("")
	public ResponseEntity<List<Transaction>> fetchAllTransactions(HttpServletRequest request, @PathVariable(CATEGORY_ID) Integer categoryId) {
		
		Integer userId = (Integer) request.getAttribute(USER_ID);
		List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}
	
	@GetMapping("/{"+TRANSACTION_ID+"}")
	public ResponseEntity<Transaction> fetchTransactionById(HttpServletRequest request, @PathVariable(CATEGORY_ID) Integer categoryId,
			@PathVariable(TRANSACTION_ID) Integer transactionId) {
		
		Integer userId = (Integer) request.getAttribute(USER_ID);
		Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
		return new ResponseEntity<>(transaction, HttpStatus.OK);
				
	}
	
	@PostMapping("")
	public ResponseEntity<Transaction> addTransaction(HttpServletRequest request, @PathVariable(CATEGORY_ID) Integer categoryId,
			@RequestBody Map<String, Object> transactionMap) {
		
		Integer userId = (Integer) request.getAttribute(USER_ID);
		Double amount = Double.valueOf(transactionMap.get(AMOUNT).toString());
		String note = (String) transactionMap.get(NOTE);
		Long date = (Long) transactionMap.get(TRANSACTION_DATE);
		Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, date);
		return new ResponseEntity<>(transaction, HttpStatus.CREATED);
	}
	
	@PutMapping("/{"+TRANSACTION_ID+"}")
	public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request,
			@PathVariable(CATEGORY_ID) Integer categoryId, @PathVariable(TRANSACTION_ID) Integer transactionId,
			@RequestBody Transaction transaction) {
		
		Integer userId = (Integer) request.getAttribute(USER_ID);
		transactionService.updateTransaction(userId, categoryId, transactionId, transaction);
		Map<String, Boolean> map = new HashMap<>();
		map.put(SUCCESS, true);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@DeleteMapping("/{"+TRANSACTION_ID+"}")
	public ResponseEntity<Map<String, Boolean>> removeTransaction(HttpServletRequest request,
																  @PathVariable(CATEGORY_ID) Integer categoryId, @PathVariable(TRANSACTION_ID) Integer transactionId) {
		
		Integer userId = (Integer) request.getAttribute(USER_ID);
		transactionService.removeTransaction(userId, categoryId, transactionId);
		Map<String, Boolean> map = new HashMap<>();
		map.put(SUCCESS, true);
		return new ResponseEntity<>(map, HttpStatus.OK);
		
	}
}
