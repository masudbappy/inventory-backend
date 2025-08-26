package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {
	@NotBlank(message = "Category name is required")
	private String name;

	public CategoryDto() {}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
}
