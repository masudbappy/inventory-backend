package com.tradeflow.inventory_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tradeflow.inventory_backend.model.PaymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CreateSaleDto {
	@NotNull(message = "Customer ID is required")
	private Long customerId;

	private Long userId; // Optional, can be set from authentication context

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@NotEmpty(message = "Sale items are required")
	@Valid
	private List<SaleItemDto> salesOrders;

	@PositiveOrZero(message = "Paid amount cannot be negative")
	private BigDecimal paidAmount = BigDecimal.ZERO;

	@PositiveOrZero(message = "Labor cost cannot be negative")
	private BigDecimal laborCost = BigDecimal.ZERO;

	@PositiveOrZero(message = "Discount amount cannot be negative")
	private BigDecimal discountAmount = BigDecimal.ZERO;

	// Payment log details (optional)
	private PaymentStatus paymentStatus;
	private String paymentMethod;

	// Constructors
	public CreateSaleDto() {}

	// Getters and Setters
	public Long getCustomerId() { return customerId; }
	public void setCustomerId(Long customerId) { this.customerId = customerId; }

	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }

	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }

	public List<SaleItemDto> getSalesOrders() { return salesOrders; }
	public void setSalesOrders(List<SaleItemDto> salesOrders) { this.salesOrders = salesOrders; }

	public BigDecimal getPaidAmount() { return paidAmount; }
	public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

	public BigDecimal getLaborCost() { return laborCost; }
	public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }

	public BigDecimal getDiscountAmount() { return discountAmount; }
	public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

	public PaymentStatus getPaymentStatus() { return paymentStatus; }
	public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}