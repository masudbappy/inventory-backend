package com.tradeflow.inventory_backend.dto.output;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SaleResponseDto {
	private Long saleId;
	private String saleCode;
	private Long customerId;
	private String customerName;
	private String customerContactNumber;
	private LocalDate date;
	private BigDecimal totalPrice;
	private BigDecimal paidAmount;
	private BigDecimal dueAmount; // Calculated: totalPrice - paidAmount
	private BigDecimal laborCost;
	private BigDecimal discountAmount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<SaleItemResponseDto> salesOrders;
	private List<PaymentLogResponseDto> paymentLogs;

	// Constructors
	public SaleResponseDto() {}

	// Getters and Setters
	public Long getSaleId() { return saleId; }
	public void setSaleId(Long saleId) { this.saleId = saleId; }

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}

	public Long getCustomerId() { return customerId; }
	public void setCustomerId(Long customerId) { this.customerId = customerId; }

	public String getCustomerName() { return customerName; }
	public void setCustomerName(String customerName) { this.customerName = customerName; }

	public String getCustomerContactNumber() { return customerContactNumber; }
	public void setCustomerContactNumber(String customerContactNumber) { this.customerContactNumber = customerContactNumber; }

	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }

	public BigDecimal getTotalPrice() { return totalPrice; }
	public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

	public BigDecimal getPaidAmount() { return paidAmount; }
	public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

	public BigDecimal getDueAmount() { return dueAmount; }
	public void setDueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; }

	public BigDecimal getLaborCost() { return laborCost; }
	public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }

	public BigDecimal getDiscountAmount() { return discountAmount; }
	public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

	public List<SaleItemResponseDto> getSalesOrders() { return salesOrders; }
	public void setSalesOrders(List<SaleItemResponseDto> salesOrders) { this.salesOrders = salesOrders; }

	public List<PaymentLogResponseDto> getPaymentLogs() { return paymentLogs; }
	public void setPaymentLogs(List<PaymentLogResponseDto> paymentLogs) { this.paymentLogs = paymentLogs; }
}