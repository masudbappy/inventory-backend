package com.tradeflow.inventory_backend.dto.output;

public class WarehouseResponseDto {
	private Long warehouseId;
	private String warehouseName;
	private String location;

	// Constructors, getters, setters

	public WarehouseResponseDto() {
	}

	public WarehouseResponseDto(Long warehouseId, String warehouseName, String location) {
		this.warehouseId = warehouseId;
		this.warehouseName = warehouseName;
		this.location = location;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
