package com.pentalog.api.service;

import java.util.List;

import com.pentalog.api.dto.CategoryDTO;
import com.pentalog.api.model.Category;

public interface CategoryService {
	/**
	 * Returns all the categories found in database.
	 * 
	 * @return list of Category
	 */
	List<Category> getAllCategories();
	
	/**
	 * Adds a category in the database with the given name and description.
	 * 	
	 * @param categoryDTO containing name and description of the category
	 * @return newly created category object
	 * @throws InvalidRequestException if the name is already taken or null
	 */
	Category addCategory(CategoryDTO categoryDTO);
}
