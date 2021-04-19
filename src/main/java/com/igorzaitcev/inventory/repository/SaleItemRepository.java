package com.igorzaitcev.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.inventory.model.SaleItem;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
	@Query("SELECT s FROM SaleItem s WHERE s.saleOrder = ?1")
	List<SaleItem> findByOrderId(Long orderId);
}
