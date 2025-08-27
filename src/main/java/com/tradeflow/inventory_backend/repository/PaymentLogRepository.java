package com.tradeflow.inventory_backend.repository;

import com.tradeflow.inventory_backend.model.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
	List<PaymentLog> findBySale_SaleId(Long saleId);
	List<PaymentLog> findBySale_Customer_CustomerId(Long customerId);
}