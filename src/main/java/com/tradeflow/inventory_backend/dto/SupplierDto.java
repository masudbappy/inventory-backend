package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class SupplierDto {
	private Long supplierId;
	@NotBlank(message = "Supplier name is required")
	private String name;

	private String contactNumber;

	private String address;
	private BigDecimal dueAmount;

	public SupplierDto() {
	}

	public SupplierDto(Long supplierId, String name, String contactNumber, String address, BigDecimal dueAmount) {
		this.supplierId = supplierId;
		this.name = name;
		this.contactNumber = contactNumber;
		this.address = address;
		this.dueAmount = dueAmount;
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

	public BigDecimal getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}
}
