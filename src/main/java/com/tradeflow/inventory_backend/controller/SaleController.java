package com.tradeflow.inventory_backend.controller;

import com.tradeflow.inventory_backend.dto.CreateSaleDto;
import com.tradeflow.inventory_backend.dto.output.SaleResponseDto;
import com.tradeflow.inventory_backend.exception.ResourceNotFoundException;
import com.tradeflow.inventory_backend.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
@PreAuthorize("hasRole('ADMIN')")
public class SaleController {

	private final SaleService saleService;

	public SaleController(SaleService saleService) {
		this.saleService = saleService;
	}

	@PostMapping
	public ResponseEntity<SaleResponseDto> createSale(@Valid @RequestBody CreateSaleDto createSaleDto)
			throws ResourceNotFoundException {
		SaleResponseDto sale = saleService.createSale(createSaleDto);
		return new ResponseEntity<>(sale, HttpStatus.CREATED);
	}

	/*@GetMapping("/{id}")
	public ResponseEntity<SaleResponseDto> getSaleById(@PathVariable Long id) {
		SaleResponseDto sale = saleService.getSaleById(id);
		return ResponseEntity.ok(sale);
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<SaleResponseDto>> getSalesByCustomer(@PathVariable Long customerId) {
		List<SaleResponseDto> sales = saleService.getSalesByCustomer(customerId);
		return ResponseEntity.ok(sales);
	}

	@GetMapping
	public ResponseEntity<Page<SaleResponseDto>> getAllSales(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sortBy", defaultValue = "saleId") String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {

		Page<SaleResponseDto> sales = saleService.getAllSales(page, size, sortBy, sortDir);
		return ResponseEntity.ok(sales);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<SaleResponseDto>> searchSales(
			@RequestParam(name = "q") String query,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sortBy", defaultValue = "saleId") String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {

		Page<SaleResponseDto> sales = saleService.searchSales(query, page, size, sortBy, sortDir);
		return ResponseEntity.ok(sales);
	}*/
}
