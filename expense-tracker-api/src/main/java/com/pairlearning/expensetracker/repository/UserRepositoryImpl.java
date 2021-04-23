package com.pairlearning.expensetracker.repository;

import com.pairlearning.expensetracker.domain.User;
import com.pairlearning.expensetracker.exceptions.EtAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private static final String SQL_CREATE = "INSERT INTO ET_USERS(FIRST_NAME, LAST_NAME, EMAIL,"
			+ " PASSWORD) VALUES(?, ?, ?, ?)";
	private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL = ?";
	private static final String SQL_FIND_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD"
			+ " FROM ET_USERS WHERE USER_ID = ?";
	private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD"
			+ " FROM ET_USERS WHERE EMAIL = ?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	private final RowMapper<User> userRowMapper = ((rs, rowNum) ->
			new User.UserBuilder(
					rs.getString("first_name"),
					rs.getString("last_name"),
					rs.getString("email"),
					rs.getString("password")).
					setUserId(rs.getInt("user_id"))
					.build());

	@Override
	public Integer create(User user) throws EtAuthException {
//		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getFirstName());
				ps.setString(2, user.getLastName());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getPassword() /*hashedPassword*/);
				return ps;
			}, keyHolder);
			return (Integer) keyHolder.getKeys().get("USER_ID");
		} catch (Exception e) {
			throw new EtAuthException("Invalid details. Failed to create account");
		}
	}

	@Override
	public User findByEmailAndPassword(String email, String password) throws EtAuthException {
		try {
			User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, email);
			
			 if (!password.equals(user.getPassword())) { throw new
			  EtAuthException("Invalid email/password"); 
			  }
			 
				/*
				 * if(!BCrypt.checkpw(password, user.getPassword())){ throw new
				 * EtAuthException("Invalid email/password"); }
				 */
			 return user;
		} catch (EmptyResultDataAccessException e) {
			throw new EtAuthException("Invalid email/password");
		}
	}

	@Override
	public Integer getCountByEmail(String email) {
		return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, email);
	}

	@Override
	public User findById(Integer userId) {
		return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, userId);
	}
}
