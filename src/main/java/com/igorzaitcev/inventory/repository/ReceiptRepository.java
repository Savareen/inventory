package com.igorzaitcev.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.inventory.model.Receipt;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
	@Query("SELECT r FROM Receipt r WHERE r.validated = ?1")
	List<Receipt> findByValidated(Boolean value);

	@Query("SELECT r FROM Receipt r WHERE r.purchaseOrder = ?1")
	Optional<Receipt> findByOrderId(Long orderId);
}
