package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.CreateSaleDto;
import com.tradeflow.inventory_backend.dto.SaleItemDto;
import com.tradeflow.inventory_backend.dto.output.SaleItemResponseDto;
import com.tradeflow.inventory_backend.dto.output.SaleResponseDto;
import com.tradeflow.inventory_backend.exception.ResourceNotFoundException;
import com.tradeflow.inventory_backend.model.Customer;
import com.tradeflow.inventory_backend.model.PaymentLog;
import com.tradeflow.inventory_backend.model.PaymentStatus;
import com.tradeflow.inventory_backend.model.Product;
import com.tradeflow.inventory_backend.model.Sale;
import com.tradeflow.inventory_backend.model.SalesOrder;
import com.tradeflow.inventory_backend.repository.CustomerRepository;
import com.tradeflow.inventory_backend.repository.PaymentLogRepository;
import com.tradeflow.inventory_backend.repository.ProductRepository;
import com.tradeflow.inventory_backend.repository.SaleRepository;
import com.tradeflow.inventory_backend.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

	public SaleResponseDto createSale(CreateSaleDto createSaleDto) throws ResourceNotFoundException {
		// Validate customer
		Customer customer = customerRepository.findById(createSaleDto.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + createSaleDto.getCustomerId()));

		// Create sale
		Sale sale = new Sale();
		sale.setSaleCode(generateSaleCode());
		sale.setDate(createSaleDto.getDate());
		// Calculate totals
		BigDecimal totalAmount = BigDecimal.valueOf(0.0);
		for (SaleItemDto item : createSaleDto.getSalesOrders()) {
			BigDecimal itemTotal = (item.getQuantity().multiply(item.getSellingPrice())).subtract(createSaleDto.getDiscountAmount());
			totalAmount.add(itemTotal);
		}
		sale.setDiscountAmount(createSaleDto.getDiscountAmount());
		sale.setLaborCost(createSaleDto.getLaborCost());
		BigDecimal netAmount = totalAmount.subtract(createSaleDto.getDiscountAmount());
		netAmount = netAmount.add(createSaleDto.getLaborCost());
		sale.setTotalPrice(netAmount);
		sale.setPaidAmount(createSaleDto.getPaidAmount());
		BigDecimal previousDue = customer.getDueAmount() != null ? customer.getDueAmount() : BigDecimal.ZERO;
		BigDecimal newDueAmount = previousDue.add(netAmount).subtract(createSaleDto.getPaidAmount());
		sale.setCustomer(customer);
		// Determine payment status
		if (previousDue.compareTo(BigDecimal.ZERO) <= 0) {
			customer.setPaymentStatus(PaymentStatus.PAID);
		} else if (sale.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
			customer.setPaymentStatus(PaymentStatus.PARTIAL);
		} else {
			customer.setPaymentStatus(PaymentStatus.NONE);
		}

		Sale savedSale = saleRepository.save(sale);

		// Create sale items (SalesOrder)
		for (SaleItemDto itemDto : createSaleDto.getSalesOrders()) {
			Product product = productRepository.findById(itemDto.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

			// Check stock availability
			if ((product.getStock().compareTo(itemDto.getQuantity()) < 0)) {
				throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
			}

			SalesOrder salesOrder = new SalesOrder();
			salesOrder.setSale(savedSale);
			salesOrder.setProduct(product);
			salesOrder.setQuantity(itemDto.getQuantity());
			salesOrder.setRate(itemDto.getSellingPrice());
			salesOrderRepository.save(salesOrder);

			// Update product stock
			product.setStock(product.getStock().subtract(itemDto.getQuantity()));
			productRepository.save(product);
		}

		// Create payment log if paid amount > 0
		if (createSaleDto.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
			PaymentLog paymentLog = new PaymentLog();
			paymentLog.setSale(savedSale);
			paymentLog.setPaymentAmount(createSaleDto.getPaidAmount());
			paymentLog.setDate(createSaleDto.getDate());
			paymentLogRepository.save(paymentLog);
		}

		// Update customer due amount
		customer.setPaymentStatus(createSaleDto.getPaymentStatus());
		customer.setDueAmount(newDueAmount);
		customerRepository.save(customer);

		return convertToResponseDto(savedSale, customer);
	}

	/*public SaleResponseDto getSaleById(Long id) {
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
	}*/

	private String generateSaleCode() {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		Long count = saleRepository.getTodaySalesCount() + 1;
		return "SALE-" + timestamp + "-" + String.format("%03d", count);
	}

	private SaleResponseDto convertToResponseDto(Sale sale, Customer customer) {
		SaleResponseDto dto = new SaleResponseDto();
		dto.setSaleId(sale.getSaleId());
		dto.setSaleCode(sale.getSaleCode());
		dto.setCustomerId(sale.getCustomer().getCustomerId());
		dto.setCustomerName(sale.getCustomer().getName());
		dto.setTotalPrice(sale.getTotalPrice());
		dto.setPaidAmount(sale.getPaidAmount());
		dto.setDueAmount(customer.getDueAmount());
		dto.setDate(sale.getDate());
		dto.setCreatedAt(sale.getCreatedAt());

		// Get sale items
		List<SalesOrder> salesOrders = salesOrderRepository.findBySale_SaleId(sale.getSaleId());
		List<SaleItemResponseDto> saleItems = salesOrders.stream().map(this::convertToSaleItemDto).collect(Collectors.toList());
		dto.setSalesOrders(saleItems);

		return dto;
	}

	private SaleItemResponseDto convertToSaleItemDto(SalesOrder salesOrder) {
		SaleItemResponseDto dto = new SaleItemResponseDto();
		dto.setSalesOrderId(salesOrder.getSalesOrderId());
		dto.setProductId(salesOrder.getProduct().getProductId());
		dto.setProductName(salesOrder.getProduct().getName());
		dto.setProductCode(salesOrder.getProduct().getProductCode());
		dto.setQuantity(salesOrder.getQuantity());
		dto.setRate(salesOrder.getRate());
		return dto;
	}
}
