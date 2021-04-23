package com.pairlearning.expensetracker.repository;

import com.pairlearning.expensetracker.domain.Category;
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
public class CategoryRepositoryImpl implements CategoryRepository {

	private static final String SQL_CREATE = "INSERT INTO et_categories (user_id, title, description) VALUES(?, ?, ?)";

	private static final String SQL_FIND_BY_ID = "SELECT c.category_id, c.user_id, c.title, c.description,"
			+ " COALESCE(SUM(t.amount), 0) total_expense FROM et_transactions t RIGHT OUTER JOIN et_categories c"
			+ " ON c.category_id = t.category_id WHERE c.user_id = ? AND c.category_id = ? GROUP BY c.category_id";

	private static final String SQL_FIND_ALL = "SELECT c.category_id, c.user_id, c.title, c.description,"
			+ " COALESCE(SUM(t.amount), 0) total_expense FROM et_transactions t RIGHT OUTER JOIN et_categories c"
			+ " ON c.category_id = t.category_id WHERE c.user_id = ? GROUP BY c.category_id";

	private static final String SQL_UPDATE = "UPDATE et_categories SET title = ?, description = ? "
			+ "WHERE category_id = ? AND user_id = ?";
	
	private static final String SQL_DELETE_CATEGORY = "DELETE FROM et_categories WHERE user_id = ? AND category_id = ?";
	
	private static final String SQL_DELETE_ALL_TRANSACTIONS = "DELETE FROM et_transactions WHERE category_id = ?";

	private static final String CATEGORY_NOT_FOUND = "category not found";

	private final RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> new Category.CategoryBuilder(
			rs.getInt(USER_ID),
			rs.getString(TITLE),
			rs.getString(DESCRIPTION))
			.setCategoryId(rs.getInt(CATEGORY_ID))
			.setTotalExpense(rs.getDouble(TOTAL_EXPENSE))
			.build());
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Category> fetchAll(Integer userId) throws EtResourceNotFoundException {
		return jdbcTemplate.query(SQL_FIND_ALL, categoryRowMapper, userId);
	}

	@Override
	public Category fetchById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
		try {
			return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, categoryRowMapper, userId, categoryId);
		}catch(Exception e) {
			throw new EtResourceNotFoundException(CATEGORY_NOT_FOUND);
		}
	}

	@Override
	public Integer create(Category category) throws EtBadRequestException {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, category.getUserId());
				ps.setString(2, category.getTitle());
				ps.setString(3, category.getDescription());
				return ps;
			}, keyHolder);
			return (Integer) keyHolder.getKey();
		} catch(Exception e) {
			throw new EtBadRequestException(INVALID_REQUEST);
		}
	}

	@Override
	public void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
		try {
			jdbcTemplate.update(SQL_UPDATE, category.getTitle(), category.getDescription(), categoryId, userId);
		}catch(Exception e) {
			throw new EtBadRequestException(INVALID_REQUEST);
		}
	}

	@Override
	public void removeById(Integer userId, Integer categoryId) {
		this.removeAllCategoryTransactions(categoryId);
		jdbcTemplate.update(SQL_DELETE_CATEGORY, userId, categoryId);
	}
	
	private void removeAllCategoryTransactions(Integer categoryId) {
		jdbcTemplate.update(SQL_DELETE_ALL_TRANSACTIONS, categoryId);
	}
}
