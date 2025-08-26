package com.tradeflow.inventory_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductDto {

	@NotBlank(message = "Product name is required")
	private String name;

	@NotBlank(message = "Product code is required")
	private String productCode;

	@NotNull(message = "Stock is required")
	@PositiveOrZero(message = "Stock must be positive or zero")
	private BigDecimal stock;

	@NotBlank(message = "Unit is required")
	private String unit;

	@NotNull(message = "Category is required")
	@Valid
	private CategoryDto category;

	@NotNull(message = "Type entity is required")
	@Valid
	private TypeDto typeEntity;

	@NotNull(message = "Buying price is required")
	@Positive(message = "Buying price must be positive")
	private BigDecimal buyingPrice;

	@NotNull(message = "Selling price is required")
	@Positive(message = "Selling price must be positive")
	private BigDecimal sellingPrice;

	@NotNull(message = "Warehouse is required")
	@Valid
	private WarehouseDto warehouse;

	@NotNull(message = "Supplier is required")
	@Valid
	private SupplierDto supplier;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate date;

	// Constructors
	public ProductDto() {}

	// Getters and Setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getProductCode() { return productCode; }
	public void setProductCode(String productCode) { this.productCode = productCode; }

	public BigDecimal getStock() { return stock; }
	public void setStock(BigDecimal stock) { this.stock = stock; }

	public String getUnit() { return unit; }
	public void setUnit(String unit) { this.unit = unit; }

	public CategoryDto getCategory() { return category; }
	public void setCategory(CategoryDto category) { this.category = category; }

	public TypeDto getTypeEntity() { return typeEntity; }
	public void setTypeEntity(TypeDto typeEntity) { this.typeEntity = typeEntity; }

	public BigDecimal getBuyingPrice() { return buyingPrice; }
	public void setBuyingPrice(BigDecimal buyingPrice) { this.buyingPrice = buyingPrice; }

	public BigDecimal getSellingPrice() { return sellingPrice; }
	public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

	public WarehouseDto getWarehouse() { return warehouse; }
	public void setWarehouse(WarehouseDto warehouse) { this.warehouse = warehouse; }

	public SupplierDto getSupplier() { return supplier; }
	public void setSupplier(SupplierDto supplier) { this.supplier = supplier; }

	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }
}