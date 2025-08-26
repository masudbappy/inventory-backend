package com.tradeflow.inventory_backend.dto.output;

import com.tradeflow.inventory_backend.model.Category;
import com.tradeflow.inventory_backend.model.Supplier;
import com.tradeflow.inventory_backend.model.Type;
import com.tradeflow.inventory_backend.model.Warehouse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductResponseDto {
	private Long productId;
	private String name;
	private String productCode;
	private BigDecimal stock;
	private String unit;
	private BigDecimal buyingPrice;
	private BigDecimal sellingPrice;
	private LocalDate date;
	private CategoryResponseDto category;
	private TypeResponseDto typeEntity;
	private WarehouseResponseDto warehouse;
	private SupplierResponseDto supplier;

	public ProductResponseDto() {
	}

	public ProductResponseDto(Long productId,
	                          String name, String productCode,
	                          BigDecimal stock, String unit,
	                          BigDecimal buyingPrice, BigDecimal sellingPrice,
	                          LocalDate date, CategoryResponseDto category,
	                          TypeResponseDto typeEntity, WarehouseResponseDto warehouse,
	                          SupplierResponseDto supplier) {
		this.productId = productId;
		this.name = name;
		this.productCode = productCode;
		this.stock = stock;
		this.unit = unit;
		this.buyingPrice = buyingPrice;
		this.sellingPrice = sellingPrice;
		this.date = date;
		this.category = category;
		this.typeEntity = typeEntity;
		this.warehouse = warehouse;
		this.supplier = supplier;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(BigDecimal buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public CategoryResponseDto getCategory() {
		return category;
	}

	public void setCategory(CategoryResponseDto category) {
		this.category = category;
	}

	public TypeResponseDto getTypeEntity() {
		return typeEntity;
	}

	public void setTypeEntity(TypeResponseDto typeEntity) {
		this.typeEntity = typeEntity;
	}

	public WarehouseResponseDto getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(WarehouseResponseDto warehouse) {
		this.warehouse = warehouse;
	}

	public SupplierResponseDto getSupplier() {
		return supplier;
	}

	public void setSupplier(SupplierResponseDto  supplier) {
		this.supplier = supplier;
	}
}
