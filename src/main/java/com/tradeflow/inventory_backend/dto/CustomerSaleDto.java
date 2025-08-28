package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CustomerSaleDto {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String address;

    @DecimalMin(value = "0.0", message = "Due amount must be non-negative")
    private BigDecimal dueAmount = BigDecimal.ZERO;

    // Constructors
    public CustomerSaleDto() {}

    // Getters and setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getDueAmount() { return dueAmount; }
    public void setDueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; }
}