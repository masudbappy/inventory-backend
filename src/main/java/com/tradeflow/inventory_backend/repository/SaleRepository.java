package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	List<Sale> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	// Or if you have a different date field name:
	// List<Sale> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	// List<Sale> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

	// Or use a custom query to be safe:
	@Query("SELECT s FROM Sale s WHERE s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt")
	List<Sale> findSalesBetweenDates(@Param("startDate") LocalDateTime startDate,
	                                 @Param("endDate") LocalDateTime endDate);
}