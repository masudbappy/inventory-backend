package com.tradeflow.inventory_backend.dto.output;

import java.time.LocalDateTime;

public class CustomerResponseDto {
	private Long customerId;
	private String name;
	private String phoneNumber;
	private String address;
	private Double dueAmount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// Constructors
	public CustomerResponseDto() {}

	// Getters and Setters
	public Long getCustomerId() { return customerId; }
	public void setCustomerId(Long customerId) { this.customerId = customerId; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }

	public Double getDueAmount() { return dueAmount; }
	public void setDueAmount(Double dueAmount) { this.dueAmount = dueAmount; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}