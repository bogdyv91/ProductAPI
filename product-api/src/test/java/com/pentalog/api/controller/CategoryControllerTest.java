package com.pentalog.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pentalog.api.dto.CategoryDTO;
import com.pentalog.api.exception.InvalidRequestException;
import com.pentalog.api.handler.RestControllerAdvice;
import com.pentalog.api.service.CategoryService;

@RunWith(SpringRunner.class)
public class CategoryControllerTest {

	@Mock
	CategoryService categoryService;

	@InjectMocks
	CategoryController categoryController;

	MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).setControllerAdvice(new RestControllerAdvice())
				.build();
	}
	
	@Test
	public void testGetCategoriesShouldReturnStatusOk() throws Exception {
		mockMvc.perform(get("/categories")).andExpect(status().isOk());
	}
	
	@Test
	public void testAddCategoryShouldReturnStatusCreated() throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		mockMvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON).content(ow.writeValueAsString(new CategoryDTO()))).andExpect(status().isCreated());
	}
	
	@Test
	public void testAddCategoryShouldThrowException() throws Exception {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		CategoryDTO category = new CategoryDTO();
		when(categoryService.addCategory(category)).thenThrow(InvalidRequestException.class);
		mockMvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON).content(ow.writeValueAsString(category))).andExpect(status().isBadRequest());
	}
}
