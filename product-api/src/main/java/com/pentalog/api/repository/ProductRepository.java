package com.pentalog.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentalog.api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findOneByName(String name);

	Optional<Product> findOneById(Integer productId);

}
