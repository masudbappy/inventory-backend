package com.tradeflow.inventory_backend.dto.output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentLogResponseDto {
	private Long paymentLogId;
	private BigDecimal amount;
	private String paymentMethod;
	private String notes;
	private LocalDateTime paymentDate;
	private LocalDateTime createdAt;

	// Constructors
	public PaymentLogResponseDto() {}

	// Getters and Setters
	public Long getPaymentLogId() { return paymentLogId; }
	public void setPaymentLogId(Long paymentLogId) { this.paymentLogId = paymentLogId; }

	public BigDecimal getAmount() { return amount; }
	public void setAmount(BigDecimal amount) { this.amount = amount; }

	public String getPaymentMethod() { return paymentMethod; }
	public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }

	public LocalDateTime getPaymentDate() { return paymentDate; }
	public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}