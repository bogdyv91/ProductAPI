package com.pentalog.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentalog.api.dto.CategoryDTO;
import com.pentalog.api.model.Category;
import com.pentalog.api.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public List<Category> getCategories() {
		return categoryService.getAllCategories();
	}

	@PostMapping
	public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO categoryDTO) {
		return new ResponseEntity<Category>(categoryService.addCategory(categoryDTO), HttpStatus.CREATED);
	}

}
