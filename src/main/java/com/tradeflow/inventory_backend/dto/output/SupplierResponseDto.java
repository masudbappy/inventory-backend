package com.tradeflow.inventory_backend.dto.output;

public class SupplierResponseDto {
	private Long supplierId;
	private String name;
	private String contactNumber;
	private String address;

	// Constructors, getters, setters

	public SupplierResponseDto() {
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
