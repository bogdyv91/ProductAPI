package com.pentalog.api.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pentalog.api.model.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

	Set<Category> findAllByNameInIgnoreCase(List<String> categoryNames);

	List<Category> findAll();

	Category findByName(String name);

}
