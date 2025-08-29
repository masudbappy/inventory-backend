package com.tradeflow.inventory_backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerPaymentDto {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String note;

    @NotNull(message = "Payment date is required")
    private LocalDate date;

    // Constructors
    public CustomerPaymentDto() {}

    // Getters and setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}