package com.pairlearning.expensetracker.repository;

import com.pairlearning.expensetracker.domain.Transaction;
import com.pairlearning.expensetracker.exceptions.EtBadRequestException;
import com.pairlearning.expensetracker.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static com.pairlearning.expensetracker.Constants.*;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

	private static final String SQL_FIND_BY_ID = "SELECT transaction_id, category_id, user_id, amount, note, transaction_date "
			+ "FROM et_transactions WHERE user_id = ? AND category_id = ? AND transaction_id = ?";
			
	
	private static final String SQL_CREATE = "INSERT INTO et_transactions (category_id, user_id, amount, note, transaction_date)"
			+ " VALUES (?, ?, ?, ?, ?)";
	
	private static final String SQL_FIND_ALL = "SELECT transaction_id, category_id, user_id, amount, note, transaction_date "
			+ "FROM et_transactions WHERE user_id = ? AND category_id = ?";
	
	private static final String SQL_UPDATE = "UPDATE et_transactions SET amount = ?, note = ?, transaction_date = ? "
			+ "WHERE user_id = ? AND category_id = ? AND transaction_id = ?"; 
	
	
	private static final String SQL_DELETE = "DELETE FROM et_transactions WHERE user_id = ? AND category_id = ? AND transaction_id = ?";

	private static final String TRANSACTION_NOT_FOUND = "transaction not found";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final RowMapper<Transaction> transactionRowMapper = ((rs, rowNum) -> new Transaction.TransactionBuilder(
			rs.getInt(CATEGORY_ID),
			rs.getInt(USER_ID),
			rs.getDouble(AMOUNT),
			rs.getString(NOTE),
			rs.getLong(TRANSACTION_DATE))
			.setTransactionId(rs.getInt(TRANSACTION_ID))
			.build());
	
	@Override
	public List<Transaction> fetchAll(Integer userId, Integer categoryId) {
		return jdbcTemplate.query(SQL_FIND_ALL, transactionRowMapper, userId, categoryId);
	}

	@Override
	public Transaction fetchById(Integer userId, Integer categoryId, Integer transactionId)
			throws EtResourceNotFoundException {
		try {
			return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, transactionRowMapper, userId, categoryId, transactionId);
		}catch(Exception e) {
			throw new EtResourceNotFoundException(TRANSACTION_NOT_FOUND);
		}
	}

	@Override
	public Integer create(Transaction transaction)
			throws EtBadRequestException {
		
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, transaction.getCategoryId());
				ps.setInt(2, transaction.getUserId());
				ps.setDouble(3, transaction.getAmount());
				ps.setString(4, transaction.getNote());
				ps.setLong(5, transaction.getTransactionDate());
				return ps;
			}, keyHolder);
			return (Integer) keyHolder.getKey();
		} catch(Exception e) {
			throw new EtBadRequestException(INVALID_REQUEST);
		}
		
	}

	@Override
	public void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
			throws EtBadRequestException {
			
		try {
			jdbcTemplate.update(SQL_UPDATE, transaction.getAmount(), transaction.getNote(), transaction.getTransactionDate(),
					userId, categoryId, transactionId);
		}catch(Exception e) {
			throw new EtBadRequestException(INVALID_REQUEST);
		}
	}

	@Override
	public void removeById(Integer userId, Integer categoryId, Integer transactionId)
			throws EtResourceNotFoundException {
		int count = jdbcTemplate.update(SQL_DELETE, userId, categoryId, transactionId);
		if(count == 0) {
			throw new EtResourceNotFoundException(TRANSACTION_NOT_FOUND);
		}
	}

}
