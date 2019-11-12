package com.pentalog.api.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.pentalog.api.converter.ProductConverter;
import com.pentalog.api.dto.ProductDTO;
import com.pentalog.api.exception.ObjectNotFoundException;
import com.pentalog.api.model.Category;
import com.pentalog.api.model.Product;
import com.pentalog.api.repository.CategoryRepository;
import com.pentalog.api.repository.ProductRepository;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

	@Mock
	ProductRepository productRepository;
	@Mock
	ProductConverter productConverter;
	@Mock
	CategoryRepository categoryRepository;

	@InjectMocks
	ProductServiceImpl productServiceImpl;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(productServiceImpl);
	}

	@Test
	public void testAddProductShouldWork() {
		Product product = new Product();
		product.setName("name");
		ProductDTO productDTO = new ProductDTO();
		when(productConverter.convertFromProductDTOtoProduct(productDTO)).thenReturn(product);
		when(productRepository.findOneByName(Mockito.anyString())).thenReturn(Optional.of(product));
		assertEquals(productServiceImpl.addProduct(productDTO), product);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void testAddProductShouldThrowObjectNotFoundException() {
		Product product = new Product();
		product.setName("name");
		ProductDTO productDTO = new ProductDTO();
		when(productConverter.convertFromProductDTOtoProduct(productDTO)).thenReturn(product);
		when(productRepository.findOneByName(Mockito.anyString())).thenReturn(Optional.empty());
		productServiceImpl.addProduct(productDTO);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void testAddProductShouldThrowObjectNotFoundExceptionBecauseOfRepositoryException() {
		Product product = new Product();
		product.setName("name");
		ProductDTO productDTO = new ProductDTO();
		when(productConverter.convertFromProductDTOtoProduct(productDTO)).thenReturn(product);
		when(productRepository.findOneByName(Mockito.anyString())).thenReturn(Optional.of(product));
		when(productRepository.save(product)).thenThrow(RuntimeException.class);
		productServiceImpl.addProduct(productDTO);
	}

	@Test
	public void testAddCategoryToProductShouldWork() {
		Product product = new Product();
		product.setName("name");
		List<String> categoryNames = new ArrayList<>();
		Set<Category> categories = new HashSet<>();
		when(productRepository.findOneById(1)).thenReturn(Optional.of(product));
		when(categoryRepository.findAllByNameInIgnoreCase(Mockito.any())).thenReturn(categories);
		productServiceImpl.addCategoryToProduct(1, categoryNames);
		verify(productRepository, times(1)).save(Mockito.any());
	}

	@Test
	public void testDeleteProductShouldWork() {
		Product product = new Product();
		product.setName("name");
		when(productRepository.findOneById(1)).thenReturn(Optional.of(product));
		productServiceImpl.removeProduct(1);
		verify(productRepository, times(1)).delete(Mockito.any());
	}

	@Test(expected = ObjectNotFoundException.class)
	public void testAddCategoryToProductShouldThrowObjectNotFoundExceptionBecauseOfInvalidProductId() {
		Product product = new Product();
		product.setName("name");
		List<String> categoryNames = new ArrayList<>();
		Set<Category> categories = new HashSet<>();
		when(productRepository.findOneById(1)).thenReturn(Optional.empty());
		when(categoryRepository.findAllByNameInIgnoreCase(Mockito.any())).thenReturn(categories);
		productServiceImpl.addCategoryToProduct(1, categoryNames);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void testAddCategoryToProductShouldThrowObjectNotFoundExceptionBecauseOfInvalidCategoryName() {
		Product product = new Product();
		product.setName("name");
		List<String> categoryNames = new ArrayList<>();
		categoryNames.add("bad name");
		Set<Category> categories = new HashSet<>();
		when(productRepository.findOneById(1)).thenReturn(Optional.of(product));
		when(categoryRepository.findAllByNameInIgnoreCase(Mockito.any())).thenReturn(categories);
		productServiceImpl.addCategoryToProduct(1, categoryNames);
	}

	@Test
	public void testRemoveCategoriesFromProductShouldWork() {
		Product product = new Product();
		product.setName("name");
		List<String> categoryNames = new ArrayList<>();
		Set<Category> categories = new HashSet<>();
		product.setCategories(categories);
		when(productRepository.findOneById(1)).thenReturn(Optional.of(product));
		when(categoryRepository.findAllByNameInIgnoreCase(Mockito.any())).thenReturn(categories);
		productServiceImpl.removeCategoriesFromProduct(1, categoryNames);
		verify(productRepository, times(1)).save(Mockito.any());
	}
	
	@Test
	public void testGetProductsForCategoriesShouldWork() {
		Set<Category> categories = new HashSet<>();
		when(categoryRepository.findAllByNameInIgnoreCase(Mockito.any())).thenReturn(categories);
		assertEquals(0,productServiceImpl.getProductsForCategories(new ArrayList<String>()).size());
	}
	
	@Test
	public void testUpdateProductByIdShouldWork() {
		Product product = new Product();
		product.setName("name");
		when(productRepository.findOneById(1)).thenReturn(Optional.of(product));
		productServiceImpl.updateProductById(1, new HashMap<>());
		verify(productRepository, times(1)).save(product);
	}
	
	@Test(expected=ObjectNotFoundException.class)
	public void testUpdateProductByIdShouldThrowExceptionPriceNotANumber() {
		Product product = new Product();
		product.setName("name");
		when(productRepository.findOneById(1)).thenReturn(Optional.of(product));
		HashMap<String,Object> map = new HashMap<>();
		map.put("price", "ab123");
		productServiceImpl.updateProductById(1, map);
		verify(productRepository, times(0)).save(product);
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void testUpdateProductByIdShouldThrowExceptionFieldNotAvailable() {
		Product product = new Product();
		product.setName("name");
		when(productRepository.findOneById(1)).thenReturn(Optional.of(product));
		HashMap<String,Object> map = new HashMap<>();
		map.put("price", "ab123");
		productServiceImpl.updateProductById(1, map);
		when(productRepository.save(Mockito.any())).thenThrow(ObjectNotFoundException.class);
		verify(productRepository, times(0)).save(product);
	}
}
