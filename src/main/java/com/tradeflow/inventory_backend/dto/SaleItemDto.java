package com.tradeflow.inventory_backend.dto;

import java.math.BigDecimal;

public class SaleItemDto {
    private ProductDto productDto;
    private BigDecimal quantity;

    public SaleItemDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
