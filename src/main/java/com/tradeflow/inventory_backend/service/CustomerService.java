package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.CustomerDto;
import com.tradeflow.inventory_backend.dto.CustomerPaymentDto;
import com.tradeflow.inventory_backend.dto.CustomerPaymentHistoryDto;
import com.tradeflow.inventory_backend.dto.output.CustomerResponseDto;
import com.tradeflow.inventory_backend.exception.ResourceNotFoundException;
import com.tradeflow.inventory_backend.model.Customer;
import com.tradeflow.inventory_backend.model.PaymentLog;
import com.tradeflow.inventory_backend.model.PaymentStatus;
import com.tradeflow.inventory_backend.repository.CustomerRepository;
import com.tradeflow.inventory_backend.repository.PaymentLogRepository;
import com.tradeflow.inventory_backend.repository.SaleRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final PaymentLogRepository paymentLogRepository;
	private final SaleRepository saleRepository;

	public CustomerService(CustomerRepository customerRepository, PaymentLogRepository paymentLogRepository, SaleRepository saleRepository) {
		this.customerRepository = customerRepository;
        this.paymentLogRepository = paymentLogRepository;
        this.saleRepository = saleRepository;
    }

	public CustomerResponseDto createCustomer(CustomerDto customerDto) {
		validateUniqueConstraints(customerDto, null);

		Customer customer = new Customer();
		customer.setCustomerId(customerDto.getCustomerId());
		customer.setName(customerDto.getName());
		customer.setContactNumber(customerDto.getPhoneNumber());
		customer.setAddress(customerDto.getAddress());
		customer.setDueAmount(customerDto.getDueAmount());
		customer.setCreatedAt(customerDto.getCreatedAt());
		customer.setUpdatedAt(customerDto.getUpdatedAt());

		Customer savedCustomer = customerRepository.save(customer);
		return convertToResponseDto(savedCustomer);
	}

	public CustomerResponseDto updateCustomer(Long id, CustomerDto customerDto) throws ResourceNotFoundException {
		Customer existingCustomer = getCustomerEntityById(id);
		validateUniqueConstraints(customerDto, id);

		existingCustomer.setName(customerDto.getName());
		existingCustomer.setContactNumber(customerDto.getPhoneNumber());
		existingCustomer.setAddress(customerDto.getAddress());
		existingCustomer.setUpdatedAt(LocalDateTime.now());

		Customer updatedCustomer = customerRepository.save(existingCustomer);
		return convertToResponseDto(updatedCustomer);
	}

	public void deleteCustomer(Long id) throws ResourceNotFoundException {
		Customer customer = getCustomerEntityById(id);
		customerRepository.delete(customer);
	}

	public List<CustomerResponseDto> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		return customers.stream()
				.map(this::convertToResponseDto)
				.collect(Collectors.toList());
	}

	public CustomerResponseDto getCustomerById(Long id) throws ResourceNotFoundException {
		Customer customer = getCustomerEntityById(id);
		return convertToResponseDto(customer);
	}

	public Page<CustomerResponseDto> searchCustomers(String query, int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("desc") ?
				Sort.by(sortBy).descending() :
				Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Customer> customerPage = customerRepository.searchByNameOrPhone(query, pageable);

		return customerPage.map(this::convertToResponseDto);
	}

	private Customer getCustomerEntityById(Long id) throws ResourceNotFoundException {
		return customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
	}

	private void validateUniqueConstraints(CustomerDto customerDto, Long excludeId) {
		if (customerDto.getPhoneNumber() != null && !customerDto.getPhoneNumber().isEmpty()) {
			customerRepository.findByContactNumber(customerDto.getPhoneNumber())
					.ifPresent(customer -> {
						if (excludeId == null || !customer.getCustomerId().equals(excludeId)) {
							throw new IllegalArgumentException("Customer with phone number '" + customerDto.getPhoneNumber() + "' already exists");
						}
					});
		}
	}

	@Transactional
	public void processPayment(CustomerPaymentDto paymentDto) throws ResourceNotFoundException {
		// Find customer
		Customer customer = customerRepository.findById(paymentDto.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + paymentDto.getCustomerId()));

		// Validate payment amount
		if (paymentDto.getAmount().compareTo(customer.getDueAmount()) > 0) {
			throw new IllegalArgumentException("Payment amount cannot exceed due amount of " + customer.getDueAmount());
		}

		// Update customer due amount
		BigDecimal newDueAmount = customer.getDueAmount().subtract(paymentDto.getAmount());
		customer.setDueAmount(newDueAmount);

		// Update payment status
		if (newDueAmount.compareTo(BigDecimal.ZERO) == 0) {
			customer.setPaymentStatus(PaymentStatus.PAID);
		} else {
			customer.setPaymentStatus(PaymentStatus.PARTIAL);
		}

		customerRepository.save(customer);

		// Create payment log
		PaymentLog paymentLog = new PaymentLog();
		paymentLog.setCustomer(customer);
		paymentLog.setAmount(paymentDto.getAmount());
		paymentLog.setPaymentMethod(paymentDto.getPaymentMethod());
		paymentLog.setNote(paymentDto.getNote());
		paymentLog.setPaymentDate(paymentDto.getDate());
		paymentLog.setCreatedAt(LocalDateTime.now());

		paymentLogRepository.save(paymentLog);
	}

	public Page<CustomerPaymentHistoryDto> getAllCustomersPaymentHistory(int page, int size, String sortBy,
																		 String sortDir, Long customerId,
																		 String transactionType, String searchQuery,
																		 String status) {

		// Normalize empty strings to null
		searchQuery = (searchQuery != null && searchQuery.trim().isEmpty()) ? null : searchQuery;
		status = (status != null && status.trim().isEmpty()) ? null : status;
		transactionType = (transactionType != null && transactionType.trim().isEmpty()) ? null : transactionType;

		Pageable pageable = PageRequest.of(page, size,
				sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

		if ("SALE".equals(transactionType)) {
			return customerRepository.findSaleHistory(customerId, searchQuery, status, pageable);
		} else if ("PAYMENT".equals(transactionType)) {
			return customerRepository.findPaymentHistory(customerId, searchQuery, status, pageable);
		} else {
			// Get both types and combine them
			List<CustomerPaymentHistoryDto> allHistory = new ArrayList<>();

			// Get sales
			Page<CustomerPaymentHistoryDto> sales = customerRepository.findSaleHistory(customerId, searchQuery, status, PageRequest.of(0, Integer.MAX_VALUE));
			allHistory.addAll(sales.getContent());

			// Get payments (only if status is null or PAYMENT)
			if (status == null || "PAYMENT".equals(status)) {
				Page<CustomerPaymentHistoryDto> payments = customerRepository.findPaymentHistory(customerId, searchQuery, status, PageRequest.of(0, Integer.MAX_VALUE));
				allHistory.addAll(payments.getContent());
			}

			// Sort combined results
			allHistory.sort((a, b) -> {
				int result = 0;
				switch (sortBy) {
					case "date":
						result = a.getDate().compareTo(b.getDate());
						break;
					case "customerName":
						result = a.getCustomerName().compareTo(b.getCustomerName());
						break;
					case "totalAmount":
						result = a.getTotalAmount().compareTo(b.getTotalAmount());
						break;
					case "amountPaid":
						result = a.getAmountPaid().compareTo(b.getAmountPaid());
						break;
					default:
						result = a.getDate().compareTo(b.getDate());
				}
				return sortDir.equalsIgnoreCase("desc") ? -result : result;
			});

			// Apply pagination
			int start = page * size;
			int end = Math.min(start + size, allHistory.size());
			List<CustomerPaymentHistoryDto> pageContent = allHistory.subList(start, end);

			return new PageImpl<>(pageContent, pageable, allHistory.size());
		}
	}

	private CustomerResponseDto convertToResponseDto(Customer customer) {
		CustomerResponseDto dto = new CustomerResponseDto();
		dto.setCustomerId(customer.getCustomerId());
		dto.setName(customer.getName());
		dto.setPhoneNumber(customer.getContactNumber());
		dto.setAddress(customer.getAddress());
		dto.setDueAmount(customer.getDueAmount());
		dto.setCreatedAt(customer.getCreatedAt());
		dto.setUpdatedAt(customer.getUpdatedAt());
		return dto;
	}
}