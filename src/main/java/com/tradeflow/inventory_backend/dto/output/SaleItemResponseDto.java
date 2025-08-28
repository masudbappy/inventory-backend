package com.tradeflow.inventory_backend.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SaleItemResponseDto {
	private Long salesOrderId;
	private Long productId;
	private String productName;
	private String productCode;
	private BigDecimal quantity;
	private BigDecimal rate;
	private BigDecimal totalAmount; // Calculated: quantity * rate
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// Constructors
	public SaleItemResponseDto() {}

	// Getters and Setters
	public Long getSalesOrderId() { return salesOrderId; }
	public void setSalesOrderId(Long salesOrderId) { this.salesOrderId = salesOrderId; }

	public Long getProductId() { return productId; }
	public void setProductId(Long productId) { this.productId = productId; }

	public String getProductName() { return productName; }
	public void setProductName(String productName) { this.productName = productName; }

	public String getProductCode() { return productCode; }
	public void setProductCode(String productCode) { this.productCode = productCode; }

	public BigDecimal getQuantity() { return quantity; }
	public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

	public BigDecimal getRate() { return rate; }
	public void setRate(BigDecimal rate) { this.rate = rate; }

	public BigDecimal getTotalAmount() { return totalAmount; }
	public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}