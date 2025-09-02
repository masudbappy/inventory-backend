package com.tradeflow.inventory_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PnLData {
	private LocalDate date;
	private LocalDate startDate;
	private LocalDate endDate;
	private String periodLabel;
	private BigDecimal totalRevenue;
	private BigDecimal totalCost;
	private BigDecimal totalProfit;
	private BigDecimal totalQuantity;
	private BigDecimal totalSales;

	// Constructor for daily P&L
	public PnLData(LocalDate date, BigDecimal totalRevenue, BigDecimal totalCost,
	               BigDecimal totalProfit, BigDecimal totalQuantity, BigDecimal totalSales) {
		this.date = date;
		this.totalRevenue = totalRevenue;
		this.totalCost = totalCost;
		this.totalProfit = totalProfit;
		this.totalQuantity = totalQuantity;
		this.totalSales = totalSales;
	}

	// Constructor for period P&L
	public PnLData(LocalDate startDate, LocalDate endDate, BigDecimal totalRevenue,
	               BigDecimal totalCost, BigDecimal totalProfit, BigDecimal totalQuantity, BigDecimal totalSales) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalRevenue = totalRevenue;
		this.totalCost = totalCost;
		this.totalProfit = totalProfit;
		this.totalQuantity = totalQuantity;
		this.totalSales = totalSales;
	}

	// Constructor for totals


	// Getters and setters
	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }

	public LocalDate getStartDate() { return startDate; }
	public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

	public LocalDate getEndDate() { return endDate; }
	public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

	public String getPeriodLabel() { return periodLabel; }
	public void setPeriodLabel(String periodLabel) { this.periodLabel = periodLabel; }

	public BigDecimal getTotalRevenue() { return totalRevenue; }
	public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

	public BigDecimal getTotalCost() { return totalCost; }
	public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

	public BigDecimal getTotalProfit() { return totalProfit; }
	public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }

	public BigDecimal getTotalQuantity() { return totalQuantity; }
	public void setTotalQuantity(BigDecimal totalQuantity) { this.totalQuantity = totalQuantity; }

	public BigDecimal getTotalSales() { return totalSales; }
	public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }
}