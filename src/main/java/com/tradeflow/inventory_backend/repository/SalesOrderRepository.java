package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
	List<SalesOrder> findBySale_SaleId(Long saleId);
}