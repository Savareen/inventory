package com.igorzaitcev.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.inventory.model.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	@Query("SELECT d FROM Delivery d WHERE d.validated = ?1")
	List<Delivery> findByValidated(Boolean value);

	@Query("SELECT d FROM Delivery d WHERE d.saleOrder = ?1")
	Optional<Delivery> findByOrderId(Long orderId);
}
