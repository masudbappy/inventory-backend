package com.tradeflow.inventory_backend.dto.output;

public class CategoryResponseDto {
	private Long categoryId;
	private String name;

	// Constructors, getters, setters

	public CategoryResponseDto() {
	}

	public CategoryResponseDto(Long categoryId,
	                           String name) {
		this.categoryId = categoryId;
		this.name = name;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
