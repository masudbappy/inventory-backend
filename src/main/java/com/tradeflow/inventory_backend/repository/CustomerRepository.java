package com.tradeflow.inventory_backend.repository;

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

	Optional<Customer> findByEmail(String email);

	Optional<Customer> findByPhoneNumber(String phoneNumber);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

	@Query("SELECT c FROM Customer c WHERE " +
			"LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
			"LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
			"c.phoneNumber LIKE CONCAT('%', :query, '%')")
	Page<Customer> searchByNameEmailOrPhone(@Param("query") String query, Pageable pageable);
}