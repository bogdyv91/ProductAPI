package com.pentalog.api.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.pentalog.api.dto.CategoryDTO;
import com.pentalog.api.exception.InvalidRequestException;
import com.pentalog.api.exception.ObjectNotFoundException;
import com.pentalog.api.repository.CategoryRepository;

@RunWith(SpringRunner.class)
public class CategoryServiceTest {

	@Mock
	CategoryRepository categoryRepository;

	@InjectMocks
	CategoryServiceImpl categoryServiceImpl;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(categoryServiceImpl);
	}
	
	@Test
	public void testGetAllcategoriesShouldWork() {
		when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
		assertEquals(categoryServiceImpl.getAllCategories().size(), 0);
	}
	
	@Test
	public void testAddCategoryShouldWork() {
		categoryServiceImpl.addCategory(new CategoryDTO());
		verify(categoryRepository, times(1)).save(Mockito.any());
	}
	
	@Test(expected = InvalidRequestException.class)
	public void testAddCategoryShouldThrowException() {
		CategoryDTO category = new CategoryDTO();
		when(categoryRepository.save(Mockito.any())).thenThrow(ObjectNotFoundException.class);
		categoryServiceImpl.addCategory(category);
		verify(categoryRepository, times(0)).save(Mockito.any());
	}
}
