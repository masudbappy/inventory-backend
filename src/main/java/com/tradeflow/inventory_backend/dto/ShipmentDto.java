package com.tradeflow.inventory_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ShipmentDto {
    private Long shipmentId;
    private String supplierName;
    private LocalDate date;
    private BigDecimal purchaseAmount;
    private BigDecimal laborCost;
    private BigDecimal transportCost;
    private BigDecimal paidAmount;
    private BigDecimal totalAmount;
    private BigDecimal dueAmount;

    public ShipmentDto() {
    }

    public ShipmentDto(Long shipmentId, String supplierName, LocalDate date, BigDecimal purchaseAmount
            , BigDecimal laborCost, BigDecimal transportCost, BigDecimal paidAmount, BigDecimal totalAmount, BigDecimal dueAmount) {
        this.shipmentId = shipmentId;
        this.supplierName = supplierName;
        this.date = date;
        this.purchaseAmount = purchaseAmount;
        this.laborCost = laborCost;
        this.transportCost = transportCost;
        this.paidAmount = paidAmount;
        this.totalAmount = totalAmount;
        this.dueAmount = dueAmount;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(BigDecimal transportCost) {
        this.transportCost = transportCost;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }
}