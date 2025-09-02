package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
	List<SalesOrder> findBySaleSaleId(Long saleId);

	// Alternative: use a custom query to be more explicit
	@Query("SELECT so FROM SalesOrder so WHERE so.sale.saleId = :saleId")
	List<SalesOrder> findBySaleId(@Param("saleId") Long saleId);

}