package com.pentalog.api.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Category {

	public Category(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Id
	private int id;

	@Column(nullable = false, unique = true)
	@Length(min = 1)
	private String name;

	private String description;

	@JsonIgnore
	@ManyToMany(mappedBy = "categories")
	private Set<Product> products;
}
