package com.tradeflow.inventory_backend.controller;

import com.tradeflow.inventory_backend.dto.SalePnLData;
import com.tradeflow.inventory_backend.dto.output.PnLResponse;
import com.tradeflow.inventory_backend.enums.PeriodType;
import com.tradeflow.inventory_backend.service.PnLCalculationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pnl")
@CrossOrigin(origins = "*")
public class PnLController {

	private final PnLCalculationService pnlCalculationService;

	public PnLController(PnLCalculationService pnlCalculationService) {
		this.pnlCalculationService = pnlCalculationService;
	}

	@GetMapping("/daily")
	public ResponseEntity<PnLResponse> getDailyPnL(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		PnLResponse response = pnlCalculationService.calculatePnL(PeriodType.DAILY, startDate, endDate);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/monthly")
	public ResponseEntity<?> getMonthlyPnL(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		// Validate date range
		if (endDate.isBefore(startDate)) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Invalid date range");
			error.put("message", "End date must be after or equal to start date");
			return ResponseEntity.badRequest().body(error);
		}

		try {
			PnLResponse response = pnlCalculationService.calculatePnL(PeriodType.MONTHLY, startDate, endDate);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Processing error");
			error.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@GetMapping("/yearly")
	public ResponseEntity<PnLResponse> getYearlyPnL(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		PnLResponse response = pnlCalculationService.calculatePnL(PeriodType.YEARLY, startDate, endDate);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/today")
	public ResponseEntity<PnLResponse> getTodayPnL() {
		LocalDate today = LocalDate.now();
		PnLResponse response = pnlCalculationService.calculatePnL(PeriodType.DAILY, today, today);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/sales-details")
	public ResponseEntity<List<SalePnLData>> getSalesDetails(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		List<SalePnLData> salesDetails = pnlCalculationService.getSalesPnLDetails(date);
		return ResponseEntity.ok(salesDetails);
	}
}