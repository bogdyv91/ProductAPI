package com.pentalog.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pentalog.api.dto.CategoryDTO;
import com.pentalog.api.exception.InvalidRequestException;
import com.pentalog.api.model.Category;
import com.pentalog.api.repository.CategoryRepository;
import com.pentalog.api.response.ApiError;

@Service
public class CategoryServiceImpl implements CategoryService {

	private @Value("${categoryNameNotValidExceptionMessage}") String categoryNameNotValidExceptionMessage;
	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Category addCategory(CategoryDTO categoryDTO) {
		Category category = new Category(categoryDTO.getName(), categoryDTO.getDescription());
		try {
			categoryRepository.save(category);
		} catch (Exception e) {
			throw new InvalidRequestException(categoryNameNotValidExceptionMessage,
					new ApiError(categoryNameNotValidExceptionMessage, category.getName(), "name"));
		}
		return categoryRepository.findByName(category.getName());
	}

}
