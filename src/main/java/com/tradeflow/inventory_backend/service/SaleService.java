package com.tradeflow.inventory_backend.service;

import com.tradeflow.inventory_backend.dto.CreateSaleDto;
import com.tradeflow.inventory_backend.dto.output.SaleResponseDto;
import com.tradeflow.inventory_backend.exception.ResourceNotFoundException;
import com.tradeflow.inventory_backend.model.*;
import com.tradeflow.inventory_backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

	private final SaleRepository saleRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final SalesOrderRepository salesOrderRepository;

	public SaleService(SaleRepository saleRepository,
					   CustomerRepository customerRepository,
					   ProductRepository productRepository,
					   SalesOrderRepository salesOrderRepository) {
		this.saleRepository = saleRepository;
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
		this.salesOrderRepository = salesOrderRepository;
	}

	@Transactional
	public SaleResponseDto createSale(CreateSaleDto createSaleDto) throws ResourceNotFoundException {
		// 1. Validate and get customer
		Customer customer = customerRepository.findById(createSaleDto.getCustomer().getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + createSaleDto.getCustomer().getCustomerId()));

		// 2. Update customer info from request
		customer.setContactNumber(createSaleDto.getCustomer().getPhone());
		customer.setAddress(createSaleDto.getCustomer().getAddress());
		customer.setDueAmount(createSaleDto.getCustomer().getDueAmount());
		customerRepository.save(customer);

		// 3. Create Sale entity
		Sale sale = new Sale();
		sale.setCustomer(customer);
		sale.setDate(createSaleDto.getDate());
		sale.setDiscountAmount(createSaleDto.getDiscount());
		sale.setLaborCost(createSaleDto.getLaborCost());
		sale.setPaidAmount(createSaleDto.getAmountPaid());
		sale.setPaymentMethod(createSaleDto.getPaymentMethod());

		// Generate sale code
		sale.setSaleCode(generateSaleCode());

		// 4. Calculate total price from products
		BigDecimal totalPrice = createSaleDto.getProducts().stream()
				.map(product -> product.getTotalPrice())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// Add labor cost and subtract discount
		totalPrice = totalPrice.add(createSaleDto.getLaborCost()).subtract(createSaleDto.getDiscount());
		sale.setTotalPrice(totalPrice);

		// 5. Save sale
		Sale savedSale = saleRepository.save(sale);

		// 6. Create sales orders and update product stock
		List<SalesOrder> salesOrders = new ArrayList<>();
		for (var productDto : createSaleDto.getProducts()) {
			// Validate product exists
			Product product = productRepository.findById(productDto.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productDto.getProductId()));

			// Check stock availability
			if (product.getStock().compareTo(productDto.getQuantity()) < 0) {
				throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
			}

			// Create sales order
			SalesOrder salesOrder = new SalesOrder();
			salesOrder.setSale(savedSale);
			salesOrder.setProduct(product);
			salesOrder.setQuantity(productDto.getQuantity());
			salesOrder.setSellingPrice(productDto.getSellingPrice());
			salesOrder.setTotalPrice(productDto.getTotalPrice());

			salesOrders.add(salesOrder);

			// Update product stock
			product.setStock(product.getStock().subtract(productDto.getQuantity()));
			productRepository.save(product);
		}

		// Save all sales orders
		salesOrderRepository.saveAll(salesOrders);
		savedSale.setSalesOrders(salesOrders);

		// 7. Convert to response DTO
		return convertToResponseDto(savedSale);
	}

	public Sale findById(Long id) throws ResourceNotFoundException {
		return saleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + id));
	}

	public List<Sale> findAll() {
		return saleRepository.findAll();
	}

	private String generateSaleCode() {
		// Generate unique sale code (you can customize this logic)
		long count = saleRepository.count() + 1;
		return "SALE-" + String.format("%06d", count);
	}

	private SaleResponseDto convertToResponseDto(Sale sale) {
		SaleResponseDto dto = new SaleResponseDto();
		dto.setSaleId(sale.getSaleId());
		dto.setSaleCode(sale.getSaleCode());
		dto.setCustomerName(sale.getCustomer().getName());
		dto.setDate(sale.getDate());
		dto.setTotalPrice(sale.getTotalPrice());
		dto.setPaidAmount(sale.getPaidAmount());
		dto.setLaborCost(sale.getLaborCost());
		dto.setDiscountAmount(sale.getDiscountAmount());
		dto.setPaymentMethod(sale.getPaymentMethod());
		dto.setCreatedAt(sale.getCreatedAt());

		// Calculate due amount
		BigDecimal dueAmount = sale.getTotalPrice().subtract(sale.getPaidAmount());
		dto.setDueAmount(dueAmount);

		return dto;
	}
}