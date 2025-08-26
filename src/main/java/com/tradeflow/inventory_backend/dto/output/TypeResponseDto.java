package com.tradeflow.inventory_backend.dto.output;

public class TypeResponseDto {
	private Long typeId;
	private String name;

	// Constructors, getters, setters

	public TypeResponseDto() {
	}

	public TypeResponseDto(Long typeId, String name) {
		this.typeId = typeId;
		this.name = name;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
