package com.igorzaitcev.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.inventory.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	@Query("SELECT c FROM Customer c WHERE c.name = ?1")
	Optional<Customer> findByName(String name);
}
