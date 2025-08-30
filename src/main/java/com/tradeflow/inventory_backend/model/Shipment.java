package com.tradeflow.inventory_backend.entity;

import com.tradeflow.inventory_backend.model.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "purchase_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal purchaseAmount;

    @Column(name = "labor_cost", precision = 10, scale = 2)
    private BigDecimal laborCost = BigDecimal.ZERO;

    @Column(name = "transport_cost", precision = 10, scale = 2)
    private BigDecimal transportCost = BigDecimal.ZERO;

    @Column(name = "paid_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name ="due_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal dueAmount;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Shipment() {
    }

    public Shipment(Long shipmentId, Supplier supplier, LocalDate date, BigDecimal purchaseAmount, BigDecimal laborCost,
                    BigDecimal transportCost, BigDecimal paidAmount, BigDecimal totalAmount,
                    BigDecimal dueAmount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.shipmentId = shipmentId;
        this.supplier = supplier;
        this.date = date;
        this.purchaseAmount = purchaseAmount;
        this.laborCost = laborCost;
        this.transportCost = transportCost;
        this.paidAmount = paidAmount;
        this.totalAmount = totalAmount;
        this.dueAmount = dueAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}