package com.pentalog.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentalog.api.dto.ProductDTO;
import com.pentalog.api.model.Product;
import com.pentalog.api.response.ApiResponse;
import com.pentalog.api.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	private ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO) {
		Product product = productService.addProduct(productDTO);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}/categories/{categories}")
	public ResponseEntity<ApiResponse> addCategoriesToProduct(@PathVariable("id") int id,
			@PathVariable("categories") List<String> categories) {
		productService.addCategoryToProduct(id, categories);
		return ResponseEntity.ok(null);
	}

	@DeleteMapping(value = "/{id}/categories/{categories}")
	public ResponseEntity<ApiResponse> removeCategoriesFromProduct(@PathVariable("id") int id,
			@PathVariable("categories") List<String> categories) {
		productService.removeCategoriesFromProduct(id, categories);
		return ResponseEntity.ok(null);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ApiResponse> removeProduct(@PathVariable("id") int id) {
		productService.removeProduct(id);
		return ResponseEntity.ok(null);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {
		return ResponseEntity.ok(productService.getProductByIdAndCheckExistance(id));
	}

	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") int id,
			@RequestBody Map<String, Object> productDTO) {
		return ResponseEntity.ok(productService.updateProductById(id, productDTO));
	}

	@GetMapping(value = "/categories/{categories}")
	public ResponseEntity<List<Product>> getProductsForCategories(
			@PathVariable("categories") List<String> categoryNames) {
		return ResponseEntity.ok(productService.getProductsForCategories(categoryNames));
	}
}
