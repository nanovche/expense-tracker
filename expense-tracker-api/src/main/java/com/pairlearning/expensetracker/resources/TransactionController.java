package com.pairlearning.expensetracker.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pairlearning.expensetracker.domain.Transaction;
import com.pairlearning.expensetracker.services.TransactionService;

@RestController
@RequestMapping("/api/categories/{categoryId}/transactions")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;

	@GetMapping("")
	public ResponseEntity<List<Transaction>> fetchAllTransactions(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId) {
		
		Integer userId = (Integer) request.getAttribute("userId");
		List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}
	
	@GetMapping("/{transactionId}")
	public ResponseEntity<Transaction> fetchTransactionById(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId,
			@PathVariable("transactionId") Integer transactionId) {
		
		Integer userId = (Integer) request.getAttribute("userId");
		Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
		return new ResponseEntity<>(transaction, HttpStatus.OK);
				
	}
	
	@PostMapping("")
	public ResponseEntity<Transaction> addTransaction(HttpServletRequest request, @PathVariable("categoryId") Integer categoryId,
			@RequestBody Map<String, Object> transactionMap) {
		
		Integer userId = (Integer) request.getAttribute("userId");
		Double amount = Double.valueOf(transactionMap.get("amount").toString());
		String note = (String) transactionMap.get("note");
		Long date = (Long) transactionMap.get("transactionDate");
		Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, date);
		return new ResponseEntity<>(transaction, HttpStatus.CREATED);
	}
	
	@PutMapping("/{transactionId}")
	public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request,
			@PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId,
			@RequestBody Transaction transaction) {
		
		Integer userId = (Integer) request.getAttribute("userId");
		transactionService.updateTransaction(userId, categoryId, transactionId, transaction);
		Map<String, Boolean> map = new HashMap<>();
		map.put("success", true);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@DeleteMapping("/{transactionId}")
	public ResponseEntity<Map<String, Boolean>> deleteTransaction(HttpServletRequest request,
			@PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId) {
		
		Integer userId = (Integer) request.getAttribute("userId");
		transactionService.removeTransaction(userId, categoryId, transactionId);
		Map<String, Boolean> map = new HashMap<>();
		map.put("success", true);
		return new ResponseEntity<>(map, HttpStatus.OK);
		
	}
}
