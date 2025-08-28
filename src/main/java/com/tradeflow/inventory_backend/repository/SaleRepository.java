package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	/*List<Sale> findByCustomer_CustomerId(Long customerId);

	Page<Sale> findByCustomer_CustomerId(Long customerId, Pageable pageable);

	@Query("SELECT s FROM Sale s WHERE s.date BETWEEN :startDate AND :endDate")
	List<Sale> findByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT s FROM Sale s WHERE " +
			"LOWER(s.customer.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
			"s.customer.contactNumber LIKE CONCAT('%', :query, '%')")
	Page<Sale> searchSales(@Param("query") String query, Pageable pageable);*/

	@Query("SELECT COUNT(s) FROM Sale s WHERE s.date = CURRENT_DATE")
	Long getTodaySalesCount();
}