package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	Optional<Warehouse> findByWarehouseNameAndLocation(String warehouseName, String location);
}
