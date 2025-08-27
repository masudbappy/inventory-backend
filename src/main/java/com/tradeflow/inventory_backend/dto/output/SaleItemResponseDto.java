package com.tradeflow.inventory_backend.dto.output;

public class SaleItemResponseDto {
	private Long salesOrderId;
	private Long productId;
	private String productName;
	private String productCode;
	private Integer quantity;
	private Double unitPrice;
	private Double discount;
	private Double totalPrice;

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

	public Integer getQuantity() { return quantity; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }

	public Double getUnitPrice() { return unitPrice; }
	public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

	public Double getDiscount() { return discount; }
	public void setDiscount(Double discount) { this.discount = discount; }

	public Double getTotalPrice() { return totalPrice; }
	public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}
