package com.tradeflow.inventory_backend.service;


import com.tradeflow.inventory_backend.dto.PnLData;
import com.tradeflow.inventory_backend.dto.SalePnLData;
import com.tradeflow.inventory_backend.dto.output.PnLResponse;
import com.tradeflow.inventory_backend.model.Product;
import com.tradeflow.inventory_backend.model.Sale;
import com.tradeflow.inventory_backend.model.SalesOrder;
import com.tradeflow.inventory_backend.repository.ProductRepository;
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
@Transactional
public class PnLCalculationService {

	private final ProductRepository productRepository;

	private final SaleRepository saleRepository;

	private final SalesOrderRepository salesOrderRepository;

	public PnLCalculationService(ProductRepository productRepository, SaleRepository saleRepository, SalesOrderRepository salesOrderRepository) {
		this.productRepository = productRepository;
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
				throw new IllegalArgumentException("Invalid period type");
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
			if (monthEnd.isAfter(endDate)) {
				monthEnd = endDate;
			}

			PnLData monthPnL = calculatePnLForPeriod(current, monthEnd);
			monthPnL.setPeriodLabel(current.getYear() + "-" + String.format("%02d", current.getMonthValue()));
			monthlyPnL.add(monthPnL);

			current = current.plusMonths(1);
		}

		return new PnLResponse(monthlyPnL, calculateTotalPnL(monthlyPnL));
	}

	private PnLResponse calculateYearlyPnL(LocalDate startDate, LocalDate endDate) {
		List<PnLData> yearlyPnL = new ArrayList<>();

		LocalDate current = startDate.withDayOfYear(1);
		while (!current.isAfter(endDate)) {
			LocalDate yearEnd = LocalDate.of(current.getYear(), 12, 31);
			if (yearEnd.isAfter(endDate)) {
				yearEnd = endDate;
			}

			PnLData yearPnL = calculatePnLForPeriod(current, yearEnd);
			yearPnL.setPeriodLabel(String.valueOf(current.getYear()));
			yearlyPnL.add(yearPnL);

			current = current.plusYears(1);
		}

		return new PnLResponse(yearlyPnL, calculateTotalPnL(yearlyPnL));
	}

	private PnLData calculatePnLForDate(LocalDate date) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 59, 59);

		List<Sale> sales = saleRepository.findByCreatedAtBetween(startOfDay, endOfDay);

		BigDecimal totalRevenue = BigDecimal.ZERO;
		BigDecimal totalCost = BigDecimal.ZERO;
		BigDecimal totalQuantity = BigDecimal.valueOf(0);

		for (Sale sale : sales) {
			List<SalesOrder> salesOrders = salesOrderRepository.findBySaleId(sale.getSaleId());

			for (SalesOrder order : salesOrders) {
				Product product = order.getProduct();

				// Calculate revenue and cost for this order
//				BigDecimal orderRevenue = product.getSellingPrice().multiply(order.getQuantity());
				BigDecimal orderCost = product.getBuyingPrice().multiply(order.getQuantity());

				totalCost = totalCost.add(orderCost);
				totalQuantity.add(order.getQuantity());
			}
			totalRevenue = totalRevenue.add(sale.getTotalPrice());

		}

		BigDecimal profit = totalRevenue.subtract(totalCost);

		return new PnLData(date, totalRevenue, totalCost, profit,
				totalQuantity, BigDecimal.valueOf(sales.size()));
	}

	private PnLData calculatePnLForPeriod(LocalDate startDate, LocalDate endDate) {
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

		List<Sale> sales = saleRepository.findSalesBetweenDates(startDateTime, endDateTime);

		BigDecimal totalRevenue = BigDecimal.ZERO;
		BigDecimal totalCost = BigDecimal.ZERO;
		BigDecimal totalQuantity = BigDecimal.valueOf(0);

		for (Sale sale : sales) {
			List<SalesOrder> salesOrders = salesOrderRepository.findBySaleId(sale.getSaleId());

			for (SalesOrder order : salesOrders) {
				Product product = order.getProduct();

//				BigDecimal orderRevenue = product.getSellingPrice().multiply(order.getQuantity());
				BigDecimal orderCost = product.getBuyingPrice().multiply(order.getQuantity());

				totalCost = totalCost.add(orderCost);
				totalQuantity.add(order.getQuantity());
			}
			totalRevenue = totalRevenue.add(sale.getTotalPrice());
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
		LocalDateTime endOfDay = date.atTime(23, 59, 59);

		List<Sale> sales = saleRepository.findSalesBetweenDates(startOfDay, endOfDay);

		return sales.stream().map(this::calculateSalePnL).collect(Collectors.toList());
	}

	private SalePnLData calculateSalePnL(Sale sale) {
		List<SalesOrder> orders = salesOrderRepository.findBySaleId(sale.getSaleId());

		BigDecimal totalRevenue = BigDecimal.ZERO;
		BigDecimal totalCost = BigDecimal.ZERO;
		BigDecimal totalQuantity = BigDecimal.valueOf(0);

		for (SalesOrder order : orders) {
			Product product = order.getProduct();

			BigDecimal orderRevenue = product.getSellingPrice().multiply(order.getQuantity());
			BigDecimal orderCost = product.getBuyingPrice().multiply(order.getQuantity());

			totalRevenue = totalRevenue.add(orderRevenue);
			totalCost = totalCost.add(orderCost);
			totalQuantity.add(order.getQuantity());
		}

		BigDecimal profit = totalRevenue.subtract(totalCost);
		BigDecimal profitMargin = totalRevenue.compareTo(BigDecimal.ZERO) > 0 ?
				profit.divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
				BigDecimal.ZERO;

		return new SalePnLData(sale, totalRevenue, totalCost, profit, profitMargin, totalQuantity);
	}
}