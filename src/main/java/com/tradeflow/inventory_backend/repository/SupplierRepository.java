package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	Optional<Supplier> findByNameAndContactNumber(String name, String contactNumber);
	Optional<Supplier> findByContactNumber(String contactNumber);
	Optional<Supplier> findByName(String name);

	@Query("SELECT s FROM Supplier s WHERE " +
			"(:searchQuery IS NULL OR :searchQuery = '' OR " +
			"LOWER(s.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
			"s.contactNumber LIKE CONCAT('%', :searchQuery, '%'))")
	Page<Supplier> findAllWithSearch(@Param("searchQuery") String searchQuery, Pageable pageable);
}