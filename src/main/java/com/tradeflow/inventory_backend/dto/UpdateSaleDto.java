package com.tradeflow.inventory_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class UpdateSaleDto {
	private LocalDate date;

	@Valid
	private List<SaleItemDto> salesOrders;

	@PositiveOrZero(message = "Labor cost cannot be negative")
	private BigDecimal laborCost;

	@PositiveOrZero(message = "Discount amount cannot be negative")
	private BigDecimal discountAmount;

	// Constructors
	public UpdateSaleDto() {}

	// Getters and Setters
	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }

	public List<SaleItemDto> getSalesOrders() { return salesOrders; }
	public void setSalesOrders(List<SaleItemDto> salesOrders) { this.salesOrders = salesOrders; }

	public BigDecimal getLaborCost() { return laborCost; }
	public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }

	public BigDecimal getDiscountAmount() { return discountAmount; }
	public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
}