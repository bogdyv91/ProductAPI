package com.pentalog.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamicUpdate(value = true)
public class Product {

	@Id
	private int id;

	@Column(unique = true)
	private String name;
	private String description;
	@NotNull
	private BigDecimal price;

	@Column(name = "created_date", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate createdDate;
	private boolean available;
	private Integer stock;

	@Column(name = "image")
	@Lob
	private byte[] image;

	@Singular
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_category", joinColumns = { @JoinColumn(name = "product_id") }, inverseJoinColumns = {
			@JoinColumn(name = "category_id") })
	private Set<Category> categories;

	@PrePersist
	private void onPersist() {
		available = true;
		createdDate = LocalDate.now();
	}
}
