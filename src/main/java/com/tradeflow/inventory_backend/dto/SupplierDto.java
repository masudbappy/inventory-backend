package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class SupplierDto {
	@NotBlank(message = "Supplier name is required")
	private String name;

	private String contactNumber;

	private String address;

	public SupplierDto() {}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getContactNumber() { return contactNumber; }
	public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
}
