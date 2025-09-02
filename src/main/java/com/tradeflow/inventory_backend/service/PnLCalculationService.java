package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.PnLData;
import com.tradeflow.inventory_backend.dto.SalePnLData;
import com.tradeflow.inventory_backend.dto.output.PnLResponse;
import com.tradeflow.inventory_backend.model.Product;
import com.tradeflow.inventory_backend.model.Sale;
import com.tradeflow.inventory_backend.model.SalesOrder;
import com.tradeflow.inventory_backend.repository.SaleRepository;
import com.tradeflow.inventory_backend.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PnLCalculationService {

	private final SaleRepository saleRepository;
	private final SalesOrderRepository salesOrderRepository;

	public PnLCalculationService(SaleRepository saleRepository, SalesOrderRepository salesOrderRepository) {
		this.saleRepository = saleRepository;
		this.salesOrderRepository = salesOrderRepository;
	}

	public PnLResponse calculatePnL(com.tradeflow.inventory_backend.enums.PeriodType periodType, LocalDate startDate, LocalDate endDate) {
		switch (periodType) {
			case DAILY:
				return calculateDailyPnL(startDate, endDate);
			case MONTHLY:
				return calculateMonthlyPnL(startDate, endDate);
			case YEARLY:
				return calculateYearlyPnL(startDate, endDate);
			default:
				throw new IllegalArgumentException("Invalid period type: " + periodType);
		}
	}

	private PnLResponse calculateDailyPnL(LocalDate startDate, LocalDate endDate) {
		List<PnLData> dailyPnL = new ArrayList<>();

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			PnLData dayPnL = calculatePnLForDate(date);
			dailyPnL.add(dayPnL);
		}

		return new PnLResponse(dailyPnL, calculateTotalPnL(dailyPnL));
	}

	private PnLResponse calculateMonthlyPnL(LocalDate startDate, LocalDate endDate) {
		List<PnLData> monthlyPnL = new ArrayList<>();

		LocalDate current = startDate.withDayOfMonth(1);

		while (!current.isAfter(endDate)) {
			LocalDate monthEnd = current.withDayOfMonth(current.lengthOfMonth());

			// Adjust boundaries to actual date range
			LocalDate periodStart = current.isBefore(startDate) ? startDate : current;
			LocalDate periodEnd = monthEnd.isAfter(endDate) ? endDate : monthEnd;

			// Only calculate if the period is valid
			if (!periodStart.isAfter(periodEnd)) {
				// Sum up daily PnL for this month
				PnLData monthPnL = sumDailyPnLForPeriod(periodStart, periodEnd);
				monthPnL.setPeriodLabel(current.getYear() + "-" + String.format("%02d", current.getMonthValue()));
				monthlyPnL.add(monthPnL);
			}

			current = current.plusMonths(1);
		}

		return new PnLResponse(monthlyPnL, calculateTotalPnL(monthlyPnL));
	}

	private PnLResponse calculateYearlyPnL(LocalDate startDate, LocalDate endDate) {
		List<PnLData> yearlyPnL = new ArrayList<>();

		int startYear = startDate.getYear();
		int endYear = endDate.getYear();

		for (int year = startYear; year <= endYear; year++) {
			LocalDate yearStart = LocalDate.of(year, 1, 1);
			LocalDate yearEnd = LocalDate.of(year, 12, 31);

			// Adjust to actual date range boundaries
			if (yearStart.isBefore(startDate)) {
				yearStart = startDate;
			}
			if (yearEnd.isAfter(endDate)) {
				yearEnd = endDate;
			}

			// Sum up daily PnL for this year
			PnLData yearPnL = sumDailyPnLForPeriod(yearStart, yearEnd);
			yearPnL.setPeriodLabel(String.valueOf(year));
			yearlyPnL.add(yearPnL);
		}

		return new PnLResponse(yearlyPnL, calculateTotalPnL(yearlyPnL));
	}

	// New method to sum daily PnL for a given period
	private PnLData sumDailyPnLForPeriod(LocalDate startDate, LocalDate endDate) {
		BigDecimal totalRevenue = BigDecimal.ZERO;
		BigDecimal totalCost = BigDecimal.ZERO;
		BigDecimal totalProfit = BigDecimal.ZERO;
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalSales = BigDecimal.ZERO;

		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			PnLData dayPnL = calculatePnLForDate(date);

			totalRevenue = totalRevenue.add(dayPnL.getTotalRevenue());
			totalCost = totalCost.add(dayPnL.getTotalCost());
			totalProfit = totalProfit.add(dayPnL.getTotalProfit());
			totalQuantity = totalQuantity.add(dayPnL.getTotalQuantity());
			totalSales = totalSales.add(dayPnL.getTotalSales());
		}

		return new PnLData(startDate, endDate, totalRevenue, totalCost,
				totalProfit, totalQuantity, totalSales);
	}

	private PnLData calculatePnLForDate(LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);

		List<Sale> sales = saleRepository.findByCreatedAtBetween(startOfDay, endOfDay);

		BigDecimal totalRevenue = BigDecimal.ZERO;
		BigDecimal totalCost = BigDecimal.ZERO;
		BigDecimal totalQuantity = BigDecimal.ZERO; // Fixed initialization

		for (Sale sale : sales) {
			List<SalesOrder> salesOrders = salesOrderRepository.findBySaleId(sale.getSaleId());

			for (SalesOrder order : salesOrders) {
				Product product = order.getProduct();
				BigDecimal orderCost = product.getBuyingPrice().multiply(order.getQuantity());

				totalCost = totalCost.add(orderCost);
				totalQuantity = totalQuantity.add(order.getQuantity()); // Fixed: was missing assignment
			}
			totalRevenue = totalRevenue.add(sale.getTotalPrice());
		}

		BigDecimal profit = totalRevenue.subtract(totalCost);

		return new PnLData(date, totalRevenue, totalCost, profit,
				totalQuantity, BigDecimal.valueOf(sales.size()));
	}

	private PnLData calculatePnLForPeriod(LocalDate startDate, LocalDate endDate) {
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);

		// Single query to get all sales in the exact period
		List<Sale> sales = saleRepository.findSalesBetweenDates(startDateTime, endDateTime);

		BigDecimal totalRevenue = BigDecimal.ZERO;
		BigDecimal totalCost = BigDecimal.ZERO;
		BigDecimal totalQuantity = BigDecimal.ZERO;

		for (Sale sale : sales) {
			totalRevenue = totalRevenue.add(sale.getTotalPrice());

			List<SalesOrder> salesOrders = salesOrderRepository.findBySaleId(sale.getSaleId());
			for (SalesOrder order : salesOrders) {
				Product product = order.getProduct();
				BigDecimal orderCost = product.getBuyingPrice().multiply(order.getQuantity());

				totalCost = totalCost.add(orderCost);
				totalQuantity = totalQuantity.add(order.getQuantity());
			}
		}

		BigDecimal profit = totalRevenue.subtract(totalCost);

		return new PnLData(startDate, endDate, totalRevenue, totalCost,
				profit, totalQuantity, BigDecimal.valueOf(sales.size()));
	}

	private PnLData calculateTotalPnL(List<PnLData> pnlDataList) {
		BigDecimal totalRevenue = pnlDataList.stream()
				.map(PnLData::getTotalRevenue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalCost = pnlDataList.stream()
				.map(PnLData::getTotalCost)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalProfit = totalRevenue.subtract(totalCost);

		BigDecimal totalQuantity = pnlDataList.stream()
				.map(PnLData::getTotalQuantity)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalSales = pnlDataList.stream()
				.map(PnLData::getTotalSales)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new PnLData(null, totalRevenue, totalCost, totalProfit, totalQuantity, totalSales);
	}

	public List<SalePnLData> getSalesPnLDetails(LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);

		List<Sale> sales = saleRepository.findSalesBetweenDates(startOfDay, endOfDay);

		return sales.stream().map(this::calculateSalePnL).collect(Collectors.toList());
	}

	private SalePnLData calculateSalePnL(Sale sale) {
		List<SalesOrder> orders = salesOrderRepository.findBySaleId(sale.getSaleId());

		BigDecimal totalRevenue = sale.getTotalPrice();
		BigDecimal totalCost = BigDecimal.ZERO;
		BigDecimal totalQuantity = BigDecimal.ZERO; // Fixed initialization

		for (SalesOrder order : orders) {
			Product product = order.getProduct();
			BigDecimal orderCost = product.getBuyingPrice().multiply(order.getQuantity());

			totalCost = totalCost.add(orderCost);
			totalQuantity = totalQuantity.add(order.getQuantity()); // Fixed: was missing assignment
		}

		BigDecimal profit = totalRevenue.subtract(totalCost);
		BigDecimal profitMargin = totalRevenue.compareTo(BigDecimal.ZERO) > 0 ?
				profit.divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
				BigDecimal.ZERO;

		return new SalePnLData(sale, totalRevenue, totalCost, profit, profitMargin, totalQuantity);
	}
}