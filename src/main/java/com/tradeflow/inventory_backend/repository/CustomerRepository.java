package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.dto.CustomerPaymentHistoryDto;
import com.tradeflow.inventory_backend.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByContactNumber(String contactNumber);

	@Query("SELECT c FROM Customer c WHERE " +
			"LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
			"c.contactNumber LIKE CONCAT('%', :query, '%')")
	Page<Customer> searchByNameOrPhone(@Param("query") String query, Pageable pageable);

	@Query("""
    SELECT new com.tradeflow.inventory_backend.dto.CustomerPaymentHistoryDto(
        s.saleCode,
        c.name,
        s.date,
        s.totalPrice,
        s.paidAmount,
        (s.totalPrice - s.paidAmount),
        CASE
            WHEN (s.totalPrice - s.paidAmount) = 0 THEN 'PAID'
            WHEN s.paidAmount > 0 THEN 'PARTIAL'
            ELSE 'UNPAID'
        END,
        'SALE',
        s.paymentMethod,
        ''
    )
    FROM Sale s JOIN s.customer c
    WHERE (:customerId IS NULL OR c.customerId = :customerId)
        AND (:searchQuery IS NULL OR :searchQuery = '' OR 
             LOWER(c.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR 
             c.contactNumber LIKE CONCAT('%', :searchQuery, '%'))
        AND (:status IS NULL OR :status = '' OR
            (:status = 'PAID' AND (s.totalPrice - s.paidAmount) = 0) OR
            (:status = 'PARTIAL' AND s.paidAmount > 0 AND (s.totalPrice - s.paidAmount) > 0) OR
            (:status = 'UNPAID' AND s.paidAmount = 0))
    """)
	Page<CustomerPaymentHistoryDto> findSaleHistory(
			@Param("customerId") Long customerId,
			@Param("searchQuery") String searchQuery,
			@Param("status") String status,
			Pageable pageable);

	@Query("""
    SELECT new com.tradeflow.inventory_backend.dto.CustomerPaymentHistoryDto(
        COALESCE(s.saleCode, 'DIRECT-PAYMENT'),
        c.name,
        p.paymentDate,
        CAST(0 AS java.math.BigDecimal),
        p.amount,
        CAST(0 AS java.math.BigDecimal),
        'PAYMENT',
        'PAYMENT',
        p.paymentMethod,
        COALESCE(p.note, '')
    )
    FROM PaymentLog p JOIN p.customer c LEFT JOIN p.sale s
    WHERE (:customerId IS NULL OR c.customerId = :customerId)
        AND (:searchQuery IS NULL OR :searchQuery = '' OR 
             LOWER(c.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR 
             c.contactNumber LIKE CONCAT('%', :searchQuery, '%'))
        AND (:status IS NULL OR :status = '' OR :status = 'PAYMENT')
    """)
	Page<CustomerPaymentHistoryDto> findPaymentHistory(
			@Param("customerId") Long customerId,
			@Param("searchQuery") String searchQuery,
			@Param("status") String status,
			Pageable pageable);

}