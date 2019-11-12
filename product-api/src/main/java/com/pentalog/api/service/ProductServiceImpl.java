package com.pentalog.api.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pentalog.api.converter.CurrencyConverter;
import com.pentalog.api.converter.ProductConverter;
import com.pentalog.api.dto.ProductDTO;
import com.pentalog.api.exception.ObjectNotFoundException;
import com.pentalog.api.model.Category;
import com.pentalog.api.model.Product;
import com.pentalog.api.repository.CategoryRepository;
import com.pentalog.api.repository.ProductRepository;
import com.pentalog.api.response.ApiError;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	private ProductConverter productConverter;
	private CategoryRepository categoryRepository;
	private CurrencyConverter currencyConverter;
	private @Value("${profilePersistanceErrorMessage}") String profilePersistanceErrorMessage;
	private @Value("${uniqueNameErrorMessage}") String uniqueNameErrorMessage;
	private @Value("${uniqueProductErrorMessage}") String uniqueProductErrorMessage;
	private @Value("${uniqueProductExceptionMessage}") String uniqueProductExceptionMessage;
	private @Value("${notAvailableProductErrorMessage}") String notAvailableProductErrorMessage;
	private @Value("${notAvailableProductExceptionMessage}") String notAvailableProductExceptionMessage;
	private @Value("${notFoundProductErrorMessage}") String notFoundProductErrorMessage;
	private @Value("${notFoundProductExceptionMessage}") String notFoundProductExceptionMessage;
	private @Value("${notFoundCategoryErroMessage}") String notFoundCategoryErroMessage;
	private @Value("${notFoundCategoryExceptionMessage}") String notFoundCategoryExceptionMessage;
	private @Value("${baseSymbol}") String baseSymbol;
	private @Value("${incorrectFieldNameErrorMessage}") String incorrectFieldNameErrorMessage;
	private @Value("${notValidPriceErrorMessage}") String notValidPriceErrorMessage;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, ProductConverter productConverter,
			CategoryRepository categoryRepository, CurrencyConverter currencyConverter) {
		this.productRepository = productRepository;
		this.productConverter = productConverter;
		this.categoryRepository = categoryRepository;
		this.currencyConverter = currencyConverter;
	}

	public Product addProduct(ProductDTO productDTO) {
		Product product = productConverter.convertFromProductDTOtoProduct(productDTO);
		try {
			productRepository.save(product);
			Optional<Product> persistedProduct = productRepository.findOneByName(product.getName());
			if (persistedProduct.isPresent()) {
				return persistedProduct.get();
			} else {
				throw new ObjectNotFoundException(profilePersistanceErrorMessage,
						new ApiError(profilePersistanceErrorMessage, product, "product"));
			}
		} catch (Exception ex) {
			throw new ObjectNotFoundException(uniqueNameErrorMessage,
					new ApiError(profilePersistanceErrorMessage, productDTO.getName(), "name"));
		}
	}

	public void addCategoryToProduct(Integer productId, List<String> categoryNames) {
		Product product = this.getProductByIdAndCheckExistance(productId);
		Set<Category> categories = this.getCategoriesByNamesAndCheckExistance(categoryNames);
		List<ApiError> errors = new ArrayList<>();
		categories.forEach(c -> {
			if (product.getCategories().contains(c)) {
				errors.add(new ApiError(uniqueProductErrorMessage, c.getName(), "category"));
			}
		});
		if (errors.size() != 0) {
			throw new ObjectNotFoundException(uniqueProductExceptionMessage, errors);
		}
		Product modifiedProduct = product.toBuilder().categories(categories).build();
		productRepository.save(modifiedProduct);
	}

	public void removeCategoriesFromProduct(Integer productId, List<String> categoryNames) {
		Product product = this.getProductByIdAndCheckExistance(productId);
		Set<Category> categories = this.getCategoriesByNamesAndCheckExistance(categoryNames);
		List<ApiError> errors = new ArrayList<>();
		categories.forEach(category -> {
			if (!product.getCategories().contains(category)) {
				errors.add(new ApiError(notAvailableProductErrorMessage, category.getName(), "category"));
			}
		});
		if (errors.size() != 0) {
			throw new ObjectNotFoundException(notAvailableProductExceptionMessage, errors);
		}
		product.getCategories().removeAll(categories);
		productRepository.save(product);
	}

	public void removeProduct(Integer productId) {
		Product product = this.getProductByIdAndCheckExistance(productId);
		productRepository.delete(product);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductByIdAndCheckExistance(Integer productId) {
		Optional<Product> product = productRepository.findOneById(productId);
		if (product.isEmpty()) {
			throw new ObjectNotFoundException(uniqueNameErrorMessage,
					new ApiError(notFoundProductErrorMessage, productId, "productId"));
		}
		return product.get();
	}

	public Product updateProductById(Integer productId, Map<String, Object> productDTO) {
		Product product = this.getProductByIdAndCheckExistance(productId);
		Field[] fields = product.getClass().getDeclaredFields();
		productDTO.forEach((k, v) -> {
			Arrays.asList(fields).forEach(field -> {
				BigDecimal newPrice = null;
				if (field.getName().equals(k)) {
					field.setAccessible(true);
					if (k.equals("price")) {
						try {
							if (productDTO.containsKey("symbol")) {
								newPrice = currencyConverter.convertPriceToBase(new BigDecimal(v.toString()),
										(String) productDTO.get("symbol"));
							} else {
								newPrice = currencyConverter.convertPriceToBase(new BigDecimal(v.toString()), "EUR");
							}
						} catch (Exception e) {
							throw new ObjectNotFoundException(notValidPriceErrorMessage,
									new ApiError(notValidPriceErrorMessage, v, "price"));
						}
					}
					try {
						if (!k.equals("price")) {
							field.set(product, v);
						} else {
							field.set(product, newPrice);
						}
					} catch (Exception e) {
						throw new ObjectNotFoundException(incorrectFieldNameErrorMessage,
								new ApiError(incorrectFieldNameErrorMessage, k, "field"));
					}
				}
			});
		});
		try {
			productRepository.save(product);
		} catch (Exception e) {
			throw new ObjectNotFoundException(profilePersistanceErrorMessage,
					new ApiError(profilePersistanceErrorMessage, product.getName(), "name"));
		}
		return getProductByIdAndCheckExistance(productId);
	}

	public List<Product> getProductsForCategories(List<String> categoryNames) {
		Set<Category> categories = this.getCategoriesByNamesAndCheckExistance(categoryNames);
		List<Product> products = new ArrayList<>();
		categories.forEach(category -> {
			products.addAll(category.getProducts());
		});
		return products;
	}

	private Set<Category> getCategoriesByNamesAndCheckExistance(List<String> categoryNames) {
		Set<Category> categories = categoryRepository.findAllByNameInIgnoreCase(categoryNames);
		if (categories.size() != categoryNames.size()) {
			List<ApiError> errors = new ArrayList<>();
			List<String> existingCategories = new ArrayList<>();
			categories.stream().forEach(x -> existingCategories.add(x.getName().toLowerCase()));
			categoryNames.forEach(category -> {
				if (!existingCategories.contains(category.toLowerCase())) {
					errors.add(new ApiError(notFoundCategoryErroMessage, category, "category"));
				}
			});
			throw new ObjectNotFoundException(notFoundCategoryExceptionMessage, errors);
		}
		return categories;
	}
}