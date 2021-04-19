package com.igorzaitcev.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.inventory.model.PurchaseItem;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
	@Query("SELECT p FROM PurchaseItem p WHERE p.purchaseOrder = ?1")
	List<PurchaseItem> findByOrderId(Long orderId);
}
