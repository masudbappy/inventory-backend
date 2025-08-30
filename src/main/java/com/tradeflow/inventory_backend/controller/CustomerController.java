package com.tradeflow.inventory_backend.controller;

import com.tradeflow.inventory_backend.dto.CustomerDto;
import com.tradeflow.inventory_backend.dto.CustomerPaymentHistoryDto;
import com.tradeflow.inventory_backend.dto.output.CustomerResponseDto;
import com.tradeflow.inventory_backend.exception.ResourceNotFoundException;
import com.tradeflow.inventory_backend.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
@PreAuthorize("hasRole('ADMIN')")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CustomerResponseDto customer = customerService.createCustomer(customerDto);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDto customerDto) throws ResourceNotFoundException {
        CustomerResponseDto customer = customerService.updateCustomer(id, customerDto);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) throws ResourceNotFoundException {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) throws ResourceNotFoundException {
        CustomerResponseDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerResponseDto>> searchCustomers(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "customerId") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {

        Page<CustomerResponseDto> customers = customerService.searchCustomers(query, page, size, sortBy, sortDir);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/payment-history")
    public ResponseEntity<Page<CustomerPaymentHistoryDto>> getAllCustomersPaymentHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false, name = "search") String searchQuery,
            @RequestParam(required = false) String status) {

        Page<CustomerPaymentHistoryDto> paymentHistory = customerService.getAllCustomersPaymentHistory(
                page, size, sortBy, sortDir, customerId, transactionType, searchQuery, status);

        return ResponseEntity.ok(paymentHistory);
    }
}