package com.tradeflow.inventory_backend.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleResponseDto {
	private Long saleId;
	private String saleCode;
	private Long customerId;
	private String customerName;
	private BigDecimal totalAmount;
	private BigDecimal totalDiscount;
	private BigDecimal netAmount;
	private BigDecimal paidAmount;
	private BigDecimal dueAmount;
	private String paymentStatus;
	private String paymentMethod;
	private String notes;
	private LocalDateTime saleDate;
	private LocalDateTime createdAt;
	private List<SaleItemResponseDto> saleItems;

	// Constructors
	public SaleResponseDto() {}

	// Getters and Setters
	public Long getSaleId() { return saleId; }
	public void setSaleId(Long saleId) { this.saleId = saleId; }

	public String getSaleCode() { return saleCode; }
	public void setSaleCode(String saleCode) { this.saleCode = saleCode; }

	public Long getCustomerId() { return customerId; }
	public void setCustomerId(Long customerId) { this.customerId = customerId; }

	public String getCustomerName() { return customerName; }
	public void setCustomerName(String customerName) { this.customerName = customerName; }

	public BigDecimal getTotalAmount() { return totalAmount; }
	public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

	public BigDecimal getTotalDiscount() { return totalDiscount; }
	public void setTotalDiscount(BigDecimal totalDiscount) { this.totalDiscount = totalDiscount; }


	public BigDecimal getNetAmount() { return netAmount; }
	public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

	public BigDecimal getPaidAmount() { return paidAmount; }
	public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

	public BigDecimal getDueAmount() { return dueAmount; }
	public void setDueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; }

	public String getPaymentStatus() { return paymentStatus; }
	public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

	public String getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }

	public LocalDateTime getSaleDate() { return saleDate; }
	public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public List<SaleItemResponseDto> getSaleItems() { return saleItems; }
	public void setSaleItems(List<SaleItemResponseDto> saleItems) { this.saleItems = saleItems; }
}
