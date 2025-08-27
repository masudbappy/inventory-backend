package com.tradeflow.inventory_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public class CreateSaleDto {
	@NotNull(message = "Customer ID is required")
	private Long customerId;

	private List<SaleItemDto> saleItems;

	@PositiveOrZero(message = "Discount cannot be negative")
	private Double totalDiscount = 0.0;

	@PositiveOrZero(message = "Tax cannot be negative")
	private Double tax = 0.0;

	@PositiveOrZero(message = "Paid amount cannot be negative")
	private Double paidAmount = 0.0;

	private String paymentMethod = "CASH";
	private String notes;

	// Constructors
	public CreateSaleDto() {}

	// Getters and Setters
	public Long getCustomerId() { return customerId; }
	public void setCustomerId(Long customerId) { this.customerId = customerId; }

	public List<SaleItemDto> getSaleItems() { return saleItems; }
	public void setSaleItems(List<SaleItemDto> saleItems) { this.saleItems = saleItems; }

	public Double getTotalDiscount() { return totalDiscount; }
	public void setTotalDiscount(Double totalDiscount) { this.totalDiscount = totalDiscount; }

	public Double getTax() { return tax; }
	public void setTax(Double tax) { this.tax = tax; }

	public Double getPaidAmount() { return paidAmount; }
	public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }

	public String getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
}
