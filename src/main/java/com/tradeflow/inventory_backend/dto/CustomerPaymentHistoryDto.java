package com.tradeflow.inventory_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerPaymentHistoryDto {

    private String saleCode;
    private String customerName;
    private LocalDate date;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal dueAmount;
    private String status; // Changed from PaymentStatus to String
    private String transactionType;
    private String paymentMethod;
    private String note;

    // Constructors
    public CustomerPaymentHistoryDto() {}

    public CustomerPaymentHistoryDto(String saleCode, String customerName, LocalDate date,
                                     BigDecimal totalAmount, BigDecimal amountPaid, BigDecimal dueAmount,
                                     String status, String transactionType, String paymentMethod, String note) {
        this.saleCode = saleCode;
        this.customerName = customerName;
        this.date = date;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.dueAmount = dueAmount;
        this.status = status;
        this.transactionType = transactionType;
        this.paymentMethod = paymentMethod;
        this.note = note;
    }

    // Getters and setters
    public String getSaleCode() { return saleCode; }
    public void setSaleCode(String saleCode) { this.saleCode = saleCode; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public BigDecimal getDueAmount() { return dueAmount; }
    public void setDueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}