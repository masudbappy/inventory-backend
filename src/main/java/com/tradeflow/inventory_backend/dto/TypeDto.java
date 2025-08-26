package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class TypeDto {
	@NotBlank(message = "Type name is required")
	private String name;

	public TypeDto() {}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
}