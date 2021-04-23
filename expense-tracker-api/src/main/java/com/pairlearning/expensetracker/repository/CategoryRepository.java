package com.pairlearning.expensetracker.repository;

import com.pairlearning.expensetracker.domain.Category;
import com.pairlearning.expensetracker.exceptions.EtBadRequestException;
import com.pairlearning.expensetracker.exceptions.EtResourceNotFoundException;

import java.util.List;

public interface CategoryRepository {

	
	List<Category> fetchAll(Integer userId) throws EtResourceNotFoundException;
	
	Category fetchById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
	
	Integer create(Category category) throws EtBadRequestException;

	void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;
	
	void removeById(Integer userId, Integer categoryId);
	
}
