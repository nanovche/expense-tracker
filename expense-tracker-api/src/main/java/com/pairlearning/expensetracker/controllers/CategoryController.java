package com.pairlearning.expensetracker.controllers;

import com.pairlearning.expensetracker.domain.Category;
import com.pairlearning.expensetracker.exceptions.EtBadRequestException;
import com.pairlearning.expensetracker.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pairlearning.expensetracker.Constants.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@GetMapping("")
	public ResponseEntity<List<Category>> fetchAllCategories(HttpServletRequest request) {
		int userId = (Integer)request.getAttribute(USER_ID);
		List<Category> categories = categoryService.fetchAllCategories(userId);
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	
	@GetMapping("/{"+CATEGORY_ID+"}")
	public ResponseEntity<Category> fetchCategoryById(HttpServletRequest request, @PathVariable Integer categoryId) {
		
		Integer userId = (Integer)request.getAttribute(USER_ID);
		Category category = categoryService.fetchCategoryById(userId, categoryId);
		return new ResponseEntity<>(category, HttpStatus.OK);
		
	}
	
	@PostMapping("")
	public ResponseEntity<Category> addCategory(HttpServletRequest request, @RequestBody Map<String, Object> categoryMap){
		
		int userId = (Integer) request.getAttribute(USER_ID);
		String title = (String)categoryMap.get(TITLE);
		String description = (String)categoryMap.get(DESCRIPTION);
		Category category = categoryService.addCategory(userId, title, description);
		return new ResponseEntity<>(category, HttpStatus.CREATED);
	}
	
	@PutMapping("/{"+CATEGORY_ID+"}")
	public ResponseEntity<Map<String, Boolean>> updateCategory(HttpServletRequest request, @PathVariable(CATEGORY_ID) Integer categoryId,
			@RequestBody Category category) throws EtBadRequestException, IOException{
	
		Integer userId = (Integer) request.getAttribute(USER_ID);
		categoryService.updateCategory(userId, categoryId, category);
		Map<String, Boolean> map = new HashMap<>();
		map.put(SUCCESS, true);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@DeleteMapping("/{"+CATEGORY_ID+"}")
	public ResponseEntity<Map<String, Boolean>> removeCategory(HttpServletRequest request, @PathVariable(CATEGORY_ID) Integer categoryId) throws EtBadRequestException {
	
		Integer userId = (Integer) request.getAttribute(USER_ID);
		categoryService.removeCategoryWithAllTransactions(userId, categoryId);
		Map<String, Boolean> map = new HashMap<>();
		map.put(SUCCESS, true);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
}
