package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class WarehouseDto {
	@NotBlank(message = "Warehouse name is required")
	private String warehouseName;

	private String location;

	public WarehouseDto() {}

	public String getWarehouseName() { return warehouseName; }
	public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }
}
