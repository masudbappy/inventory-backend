package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.CreateSaleDto;
import com.tradeflow.inventory_backend.dto.SaleItemDto;
import com.tradeflow.inventory_backend.dto.output.SaleItemResponseDto;
import com.tradeflow.inventory_backend.dto.output.SaleResponseDto;
import com.tradeflow.inventory_backend.exception.ResourceNotFoundException;
import com.tradeflow.inventory_backend.model.*;
import com.tradeflow.inventory_backend.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaleService {

	private final SaleRepository saleRepository;
	private final SalesOrderRepository salesOrderRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final PaymentLogRepository paymentLogRepository;

	public SaleService(SaleRepository saleRepository, SalesOrderRepository salesOrderRepository,
	                   CustomerRepository customerRepository, ProductRepository productRepository,
	                   PaymentLogRepository paymentLogRepository) {
		this.saleRepository = saleRepository;
		this.salesOrderRepository = salesOrderRepository;
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
		this.paymentLogRepository = paymentLogRepository;
	}

	public SaleResponseDto createSale(CreateSaleDto createSaleDto) {
		// Validate customer
		Customer customer = customerRepository.findById(createSaleDto.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + createSaleDto.getCustomerId()));

		// Create sale
		Sale sale = new Sale();
		sale.setSaleCode(generateSaleCode());
		sale.setCustomer(customer);
		sale.setDate(LocalDateTime.now());
		sale.setCreatedAt(LocalDateTime.now());
		sale.setUpdatedAt(LocalDateTime.now());
		sale.setNotes(createSaleDto.getNotes());

		// Calculate totals
		double totalAmount = 0.0;
		for (SaleItemDto item : createSaleDto.getSaleItems()) {
			double itemTotal = (item.getQuantity() * item.getUnitPrice()) - item.getDiscount();
			totalAmount += itemTotal;
		}

		sale.setTotalAmount(totalAmount);
		sale.setTotalDiscount(createSaleDto.getTotalDiscount());
		sale.setTax(createSaleDto.getTax());

		double netAmount = totalAmount - createSaleDto.getTotalDiscount() + createSaleDto.getTax();
		sale.setNetAmount(netAmount);
		sale.setPaidAmount(createSaleDto.getPaidAmount());
		sale.setDueAmount(netAmount - createSaleDto.getPaidAmount());

		// Determine payment status
		if (sale.getDueAmount() <= 0) {
			sale.setPaymentStatus(PaymentStatus.PAID);
		} else if (sale.getPaidAmount() > 0) {
			sale.setPaymentStatus(PaymentStatus.PARTIAL);
		} else {
			sale.setPaymentStatus(PaymentStatus.PENDING);
		}

		Sale savedSale = saleRepository.save(sale);

		// Create sale items (SalesOrder)
		for (SaleItemDto itemDto : createSaleDto.getSaleItems()) {
			Product product = productRepository.findById(itemDto.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

			// Check stock availability
			if (product.getStockQuantity() < itemDto.getQuantity()) {
				throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
			}

			SalesOrder salesOrder = new SalesOrder();
			salesOrder.setSale(savedSale);
			salesOrder.setProduct(product);
			salesOrder.setQuantity(itemDto.getQuantity());
			salesOrder.setUnitPrice(itemDto.getUnitPrice());
			salesOrder.setDiscount(itemDto.getDiscount());
			salesOrder.setTotalPrice((itemDto.getQuantity() * itemDto.getUnitPrice()) - itemDto.getDiscount());

			salesOrderRepository.save(salesOrder);

			// Update product stock
			product.setStockQuantity(product.getStockQuantity() - itemDto.getQuantity());
			productRepository.save(product);
		}

		// Create payment log if paid amount > 0
		if (createSaleDto.getPaidAmount() > 0) {
			PaymentLog paymentLog = new PaymentLog();
			paymentLog.setSale(savedSale);
			paymentLog.setAmount(createSaleDto.getPaidAmount());
			paymentLog.setPaymentMethod(createSaleDto.getPaymentMethod());
			paymentLog.setPaymentDate(LocalDateTime.now());
			paymentLog.setCreatedAt(LocalDateTime.now());
			paymentLogRepository.save(paymentLog);
		}

		// Update customer due amount
		customer.setDueAmount((customer.getDueAmount() != null ? customer.getDueAmount() : 0.0) + sale.getDueAmount());
		customerRepository.save(customer);

		return convertToResponseDto(savedSale);
	}

	public SaleResponseDto getSaleById(Long id) {
		Sale sale = saleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
		return convertToResponseDto(sale);
	}

	public List<SaleResponseDto> getSalesByCustomer(Long customerId) {
		List<Sale> sales = saleRepository.findByCustomer_CustomerId(customerId);
		return sales.stream().map(this::convertToResponseDto).collect(Collectors.toList());
	}

	public Page<SaleResponseDto> searchSales(String query, int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("desc") ?
				Sort.by(sortBy).descending() :
				Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Sale> salePage = saleRepository.searchSales(query, pageable);

		return salePage.map(this::convertToResponseDto);
	}

	public Page<SaleResponseDto> getAllSales(int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("desc") ?
				Sort.by(sortBy).descending() :
				Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Sale> salePage = saleRepository.findAll(pageable);

		return salePage.map(this::convertToResponseDto);
	}

	private String generateSaleCode() {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		Long count = saleRepository.getTodaySalesCount() + 1;
		return "SALE-" + timestamp + "-" + String.format("%03d", count);
	}

	private SaleResponseDto convertToResponseDto(Sale sale) {
		SaleResponseDto dto = new SaleResponseDto();
		dto.setSaleId(sale.getSaleId());
		dto.setSaleCode(sale.getSaleCode());
		dto.setCustomerId(sale.getCustomer().getCustomerId());
		dto.setCustomerName(sale.getCustomer().getName());
		dto.setTotalAmount(sale.getTotalPrice());
		dto.setTotalDiscount(sale.getTotalDiscount());
		dto.setTax(sale.getTax());
		dto.setNetAmount(sale.getNetAmount());
		dto.setPaidAmount(sale.getPaidAmount());
		dto.setDueAmount(sale.getDueAmount());
		dto.setPaymentStatus(sale.getPaymentStatus().toString());
		dto.setNotes(sale.getNotes());
		dto.setSaleDate(sale.getSaleDate());
		dto.setCreatedAt(sale.getCreatedAt());

		// Get sale items
		List<SalesOrder> salesOrders = salesOrderRepository.findBySale_SaleId(sale.getSaleId());
		List<SaleItemResponseDto> saleItems = salesOrders.stream().map(this::convertToSaleItemDto).collect(Collectors.toList());
		dto.setSaleItems(saleItems);

		return dto;
	}

	private SaleItemResponseDto convertToSaleItemDto(SalesOrder salesOrder) {
		SaleItemResponseDto dto = new SaleItemResponseDto();
		dto.setSalesOrderId(salesOrder.getSalesOrderId());
		dto.setProductId(salesOrder.getProduct().getProductId());
		dto.setProductName(salesOrder.getProduct().getName());
		dto.setProductCode(salesOrder.getProduct().getProductCode());
		dto.setQuantity(salesOrder.getQuantity());
		dto.setUnitPrice(salesOrder.getUnitPrice());
		dto.setDiscount(salesOrder.getDiscount());
		dto.setTotalPrice(salesOrder.getTotalPrice());
		return dto;
	}
}