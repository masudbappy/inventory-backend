package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
/*
	List<Sale> findByCustomer_CustomerId(Long customerId);

	Page<Sale> findByCustomer_CustomerId(Long customerId, Pageable pageable);

	Optional<Sale> findBySaleCode(String saleCode);

	@Query("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
	List<Sale> findBySaleDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("SELECT s FROM Sale s WHERE " +
			"LOWER(s.saleCode) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
			"LOWER(s.customer.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
			"s.customer.phoneNumber LIKE CONCAT('%', :query, '%')")
	Page<Sale> searchSales(@Param("query") String query, Pageable pageable);

	@Query("SELECT COUNT(s) FROM Sale s WHERE DATE(s.saleDate) = CURRENT_DATE")
	Long getTodaySalesCount();*/
}