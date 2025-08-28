package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class SaleItemDto {
	@NotNull(message = "Product ID is required")
	private Long productId;

	@Positive(message = "Quantity must be positive")
	private BigDecimal quantity;

	@Positive(message = "Unit price must be positive")
	private BigDecimal sellingPrice;

	// Constructors
	public SaleItemDto() {
	}

	public SaleItemDto(Long productId, BigDecimal quantity, BigDecimal sellingPrice) {
		this.productId = productId;
		this.quantity = quantity;
		this.sellingPrice = sellingPrice;
	}

	// Getters and Setters
	public Long getProductId() { return productId; }
	public void setProductId(Long productId) { this.productId = productId; }

	public BigDecimal getQuantity() { return quantity; }
	public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

	public BigDecimal getSellingPrice() { return sellingPrice; }
	public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }
}
