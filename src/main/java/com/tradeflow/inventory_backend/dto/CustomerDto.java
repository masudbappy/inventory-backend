package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerDto {
	@NotBlank(message = "Customer name is required")
	private String name;

	@Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
	private String phoneNumber;

	private String address;
	private Double dueAmount;

	// Constructors
	public CustomerDto() {}

	public CustomerDto(String name, String phoneNumber, String address, Double dueAmount) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.dueAmount = dueAmount;
	}

	// Getters and Setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}
}
