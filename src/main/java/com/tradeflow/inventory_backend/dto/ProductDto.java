package com.tradeflow.inventory_backend.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductDto {

	@NotBlank(message = "Product name is required")
	@Size(max = 255, message = "Product name must not exceed 255 characters")
	private String name;

	@Size(max = 100, message = "Product code must not exceed 100 characters")
	private String productCode;

	@Size(max = 100, message = "Type must not exceed 100 characters")
	private String type;

	@DecimalMin(value = "0.0", inclusive = true, message = "Stock must be non-negative")
	private BigDecimal stock = BigDecimal.ZERO;

	@DecimalMin(value = "0.0", inclusive = false, message = "Buying price must be greater than 0")
	private BigDecimal buyingPrice;

	@DecimalMin(value = "0.0", inclusive = false, message = "Selling price must be greater than 0")
	private BigDecimal sellingPrice;

	@Size(max = 50, message = "Unit must not exceed 50 characters")
	private String unit;

	@DecimalMin(value = "0.0", inclusive = true, message = "Low stock threshold must be non-negative")
	private BigDecimal lowStockThreshold = BigDecimal.ZERO;

	private Long warehouseId;
	private Long categoryId;
	private Long supplierId;
	private Long typeId;

	// Constructors
	public ProductDto() {}

	// Getters and Setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getProductCode() { return productCode; }
	public void setProductCode(String productCode) { this.productCode = productCode; }

	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	public BigDecimal getStock() { return stock; }
	public void setStock(BigDecimal stock) { this.stock = stock; }

	public BigDecimal getBuyingPrice() { return buyingPrice; }
	public void setBuyingPrice(BigDecimal buyingPrice) { this.buyingPrice = buyingPrice; }

	public BigDecimal getSellingPrice() { return sellingPrice; }
	public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

	public String getUnit() { return unit; }
	public void setUnit(String unit) { this.unit = unit; }

	public BigDecimal getLowStockThreshold() { return lowStockThreshold; }
	public void setLowStockThreshold(BigDecimal lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

	public Long getWarehouseId() { return warehouseId; }
	public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }

	public Long getCategoryId() { return categoryId; }
	public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

	public Long getSupplierId() { return supplierId; }
	public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

	public Long getTypeId() { return typeId; }
	public void setTypeId(Long typeId) { this.typeId = typeId; }
}
