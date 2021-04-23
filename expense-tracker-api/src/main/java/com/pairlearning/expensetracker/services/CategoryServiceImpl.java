package com.pairlearning.expensetracker.services;

import com.pairlearning.expensetracker.domain.Category;
import com.pairlearning.expensetracker.exceptions.EtBadRequestException;
import com.pairlearning.expensetracker.exceptions.EtResourceNotFoundException;
import com.pairlearning.expensetracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public List<Category> fetchAllCategories(Integer userId) {
		return categoryRepository.fetchAll(userId);
	}

	@Override
	public Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
		return categoryRepository.fetchById(userId, categoryId);
	}

	@Override
	public Category addCategory(Integer userId, String title, String description) throws EtBadRequestException {
		Category category = new Category.
				CategoryBuilder(userId, title, description).
				build();
		int categoryId = categoryRepository.create(category);
		return categoryRepository.fetchById(userId, categoryId);
	}

	@Override
	public void updateCategory(Integer userId, Integer categoryId, Category category)
			throws EtBadRequestException {
		categoryRepository.update(userId, categoryId, category);
	}

	@Override
	public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId)
			throws EtResourceNotFoundException {
		this.fetchCategoryById(userId, categoryId);
		categoryRepository.removeById(userId, categoryId);	
	}
}
