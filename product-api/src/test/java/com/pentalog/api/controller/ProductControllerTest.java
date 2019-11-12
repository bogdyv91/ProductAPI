package com.pentalog.api.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pentalog.api.dto.ProductDTO;
import com.pentalog.api.exception.ImpossibleToConvertPriceException;
import com.pentalog.api.exception.ObjectNotFoundException;
import com.pentalog.api.handler.RestControllerAdvice;
import com.pentalog.api.model.Product;
import com.pentalog.api.service.ProductService;

@RunWith(SpringRunner.class)
public class ProductControllerTest {

	@Mock
	ProductService productService;

	@InjectMocks
	ProductController productController;

	MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController).setControllerAdvice(new RestControllerAdvice())
				.build();
	}

	@Test
	public void testInsertNewCategoryShouldReturnOk() throws Exception {
		mockMvc.perform(put("/products/1/categories/Laptops")).andExpect(status().isOk());
	}

	@Test
	public void testInsertNewCategoryShouldThrowObjectNotFound() throws Exception {
		doThrow(ObjectNotFoundException.class).when(productService).addCategoryToProduct(Mockito.anyInt(),
				Mockito.any());
		mockMvc.perform(put("/products/1/categories/Laptops")).andExpect(status().isNotFound());
	}

	@Test
	public void testAddProductShouldReturnOk() throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(new ProductDTO()))).andExpect(status().isCreated());
	}

	@Test
	public void testAddProductShouldThrowImpossibleToConvert() throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		doThrow(ImpossibleToConvertPriceException.class).when(productService).addProduct(Mockito.any());
		mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(new ProductDTO()))).andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteCategoryShouldReturnOk() throws Exception {
		mockMvc.perform(delete("/products/1/categories/Laptops")).andExpect(status().isOk());
	}

	@Test
	public void testDeleteCategoryShouldReturnObjectNotFound() throws Exception {
		doThrow(ObjectNotFoundException.class).when(productService).removeCategoriesFromProduct(Mockito.anyInt(),
				Mockito.any());
		mockMvc.perform(delete("/products/1/categories/Laptops")).andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteProductShouldWork() throws Exception {
		mockMvc.perform(delete("/products/1")).andExpect(status().isOk());
	}

	@Test
	public void testGetProductByIdShouldWork() throws Exception {
		Product product = new Product();
		product.setName("name");
		product.setId(1);
		when(productService.getProductByIdAndCheckExistance(Mockito.anyInt())).thenReturn(product);
		mockMvc.perform(get("/products/1")).andExpect(status().isOk());
	}

	@Test
	public void testGetProductShouldThrowException() throws Exception {
		when(productService.getProductByIdAndCheckExistance(1)).thenThrow(ObjectNotFoundException.class);
		mockMvc.perform(get("/products/1")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetAllProductsShouldWork() throws Exception {
		mockMvc.perform(get("/products")).andExpect(status().isOk());
	}

	@Test
	public void testUpdateProductShouldWOrk() throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Map<Object, String> map = new HashMap<>();
		mockMvc.perform(
				patch("/products/1").contentType(MediaType.APPLICATION_JSON).content(ow.writeValueAsString(map)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testGetProductsForCategoryShouldWork() throws Exception {
		mockMvc.perform(get("/products/categories/categorie")).andExpect(status().isOk());

	}
}
