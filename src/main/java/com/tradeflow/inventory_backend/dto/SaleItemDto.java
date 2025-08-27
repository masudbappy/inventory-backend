package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SaleItemDto {
	@NotNull(message = "Product ID is required")
	private Long productId;

	@Positive(message = "Quantity must be positive")
	private Integer quantity;

	@Positive(message = "Unit price must be positive")
	private Double unitPrice;

	private Double discount = 0.0;

	// Constructors
	public SaleItemDto() {}

	public SaleItemDto(Long productId, Integer quantity, Double unitPrice, Double discount) {
		this.productId = productId;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.discount = discount;
	}

	// Getters and Setters
	public Long getProductId() { return productId; }
	public void setProductId(Long productId) { this.productId = productId; }

	public Integer getQuantity() { return quantity; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }

	public Double getUnitPrice() { return unitPrice; }
	public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

	public Double getDiscount() { return discount; }
	public void setDiscount(Double discount) { this.discount = discount; }
}
