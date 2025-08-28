package com.tradeflow.inventory_backend.dto.output;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SaleResponseDto {

	private Long saleId;
	private String saleCode;
	private String customerName;
	private LocalDate date;
	private BigDecimal totalPrice;
	private BigDecimal paidAmount;
	private BigDecimal laborCost;
	private BigDecimal discountAmount;
	private String paymentMethod;
	private BigDecimal dueAmount;
	private LocalDateTime createdAt;

	// Constructors
	public SaleResponseDto() {}

	// Getters and setters
	public Long getSaleId() { return saleId; }
	public void setSaleId(Long saleId) { this.saleId = saleId; }

	public String getSaleCode() { return saleCode; }
	public void setSaleCode(String saleCode) { this.saleCode = saleCode; }

	public String getCustomerName() { return customerName; }
	public void setCustomerName(String customerName) { this.customerName = customerName; }

	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }

	public BigDecimal getTotalPrice() { return totalPrice; }
	public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

	public BigDecimal getPaidAmount() { return paidAmount; }
	public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

	public BigDecimal getLaborCost() { return laborCost; }
	public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }

	public BigDecimal getDiscountAmount() { return discountAmount; }
	public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

	public String getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

	public BigDecimal getDueAmount() { return dueAmount; }
	public void setDueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}