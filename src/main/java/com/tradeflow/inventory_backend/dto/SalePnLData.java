package com.tradeflow.inventory_backend.dto;

import com.tradeflow.inventory_backend.model.Sale;

import java.math.BigDecimal;

public class SalePnLData {
	private Sale sale;
	private BigDecimal totalRevenue;
	private BigDecimal totalCost;
	private BigDecimal totalProfit;
	private BigDecimal profitMargin;
	private BigDecimal totalQuantity;

	public SalePnLData(Sale sale, BigDecimal totalRevenue, BigDecimal totalCost,
	                   BigDecimal totalProfit, BigDecimal profitMargin, BigDecimal totalQuantity) {
		this.sale = sale;
		this.totalRevenue = totalRevenue;
		this.totalCost = totalCost;
		this.totalProfit = totalProfit;
		this.profitMargin = profitMargin;
		this.totalQuantity = totalQuantity;
	}

	// Getters and setters
	public Sale getSale() { return sale; }
	public void setSale(Sale sale) { this.sale = sale; }

	public BigDecimal getTotalRevenue() { return totalRevenue; }
	public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

	public BigDecimal getTotalCost() { return totalCost; }
	public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

	public BigDecimal getTotalProfit() { return totalProfit; }
	public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }

	public BigDecimal getProfitMargin() { return profitMargin; }
	public void setProfitMargin(BigDecimal profitMargin) { this.profitMargin = profitMargin; }

	public BigDecimal getTotalQuantity() { return totalQuantity; }
	public void setTotalQuantity(BigDecimal totalQuantity) { this.totalQuantity = totalQuantity; }
}