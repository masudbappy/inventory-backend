package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    boolean existsByProductCode(String productCode);
    // Use the correct Category primary key property name
    List<Product> findByCategoryCategoryId(Long categoryId);

    // Alternative: Use @Query annotation to be explicit
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") Long categoryId);

    // Similarly for other relationships - use correct property names
    List<Product> findBySupplierSupplierId(Long supplierId);

    @Query("SELECT p FROM Product p WHERE p.supplier.supplierId = :supplierId")
    List<Product> findProductsBySupplierId(@Param("supplierId") Long supplierId);

    List<Product> findByWarehouseWarehouseId(Long warehouseId);

    @Query("SELECT p FROM Product p WHERE p.warehouse.warehouseId = :warehouseId")
    List<Product> findProductsByWarehouseId(@Param("warehouseId") Long warehouseId);

    List<Product> findByTypeEntityTypeId(Long typeId);

    @Query("SELECT p FROM Product p WHERE p.typeEntity.typeId = :typeId")
    List<Product> findProductsByTypeId(@Param("typeId") Long typeId);

    // Additional useful queries
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByProductCodeContainingIgnoreCase(String productCode);

    @Query("SELECT p FROM Product p WHERE p.stock <= p.lowStockThreshold")
    List<Product> findLowStockProducts();

    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.productCode) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.typeEntity.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Product> searchByNameOrProductCodeOrType(@Param("query") String query, Pageable pageable);
}