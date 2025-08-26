package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
	Optional<Supplier> findByNameAndContactNumber(String name, String contactNumber);
}
