package com.tradeflow.inventory_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CreateSaleDto {

	@Valid
	@NotNull(message = "Customer information is required")
	private CustomerSaleDto customer;

	@NotNull(message = "Sale date is required")
	private LocalDate date;

	@Valid
	private List<ProductSaleDto> products;

	@DecimalMin(value = "0.0", message = "Discount must be non-negative")
	private BigDecimal discount = BigDecimal.ZERO;

	@DecimalMin(value = "0.0", message = "Labor cost must be non-negative")
	private BigDecimal laborCost = BigDecimal.ZERO;

	@NotNull(message = "Amount paid is required")
	@DecimalMin(value = "0.0", message = "Amount paid must be non-negative")
	private BigDecimal amountPaid;

	@NotBlank(message = "Payment method is required")
	private String paymentMethod;

	// Constructors
	public CreateSaleDto() {}

	// Getters and setters
	public CustomerSaleDto getCustomer() { return customer; }
	public void setCustomer(CustomerSaleDto customer) { this.customer = customer; }

	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }

	public List<ProductSaleDto> getProducts() { return products; }
	public void setProducts(List<ProductSaleDto> products) { this.products = products; }

	public BigDecimal getDiscount() { return discount; }
	public void setDiscount(BigDecimal discount) { this.discount = discount; }

	public BigDecimal getLaborCost() { return laborCost; }
	public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }

	public BigDecimal getAmountPaid() { return amountPaid; }
	public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

	public String getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}