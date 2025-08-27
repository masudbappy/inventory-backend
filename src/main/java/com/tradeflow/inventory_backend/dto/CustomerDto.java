package com.tradeflow.inventory_backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerDto {
	private Long customerId;
	@NotBlank(message = "Customer name is required")
	private String name;

	@Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
	private String phoneNumber;

	private String address;
	private BigDecimal dueAmount;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	// Constructors
	public CustomerDto() {}

	public CustomerDto(Long customerId, String name, String phoneNumber, String address, BigDecimal dueAmount) {
		this.customerId = customerId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.dueAmount = dueAmount;
	}

	// Getters and Setters

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }

	public BigDecimal getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
