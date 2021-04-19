package com.pairlearning.expensetracker.repository;

import java.io.IOException;
import java.util.List;

import com.pairlearning.expensetracker.domain.Category;
import com.pairlearning.expensetracker.exceptions.EtBadRequestException;
import com.pairlearning.expensetracker.exceptions.EtResourceNotFoundException;

public interface CategoryRepository {

	
	List<Category> findAll(Integer userId) throws EtResourceNotFoundException;
	
	Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
	
	Integer create(Integer userId, String title, String description) throws EtBadRequestException;
	
	void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;
	
	void removeById(Integer userId, Integer categoryId);
	
}
