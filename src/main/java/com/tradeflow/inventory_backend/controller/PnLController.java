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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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


	@GetMapping
	public ResponseEntity<?> getPnL(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) PeriodType groupBy) {

		// Validate date range
		if (endDate.isBefore(startDate)) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Invalid date range");
			error.put("message", "End date must be after or equal to start date");
			return ResponseEntity.badRequest().body(error);
		}

		try {
			// Auto-determine period type if not specified
			PeriodType periodType = groupBy;
			if (periodType == null) {
				periodType = determinePeriodType(startDate, endDate);
			}

			PnLResponse response = pnlCalculationService.calculatePnL(periodType, startDate, endDate);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Processing error");
			error.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}


	@GetMapping("/today")
	public ResponseEntity<?> getTodayPnL() {
		LocalDate today = LocalDate.now();
		try {
			PnLResponse response = pnlCalculationService.calculatePnL(PeriodType.DAILY, today, today);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "Processing error");
			error.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	private PeriodType determinePeriodType(LocalDate fromDate, LocalDate toDate) {
		long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);

		if (daysBetween <= 31) {
			return PeriodType.DAILY;
		} else if (daysBetween <= 365) {
			return PeriodType.MONTHLY;
		} else {
			return PeriodType.YEARLY;
		}
	}
}