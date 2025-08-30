package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("SELECT s FROM Shipment s JOIN s.supplier sup WHERE " +
            "(:searchQuery IS NULL OR :searchQuery = '' OR " +
            "LOWER(sup.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    Page<Shipment> findAllWithSearch(@Param("searchQuery") String searchQuery, Pageable pageable);
}