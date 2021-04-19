package com.igorzaitcev.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igorzaitcev.inventory.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
	@Query("SELECT v FROM Vendor v WHERE v.name = ?1")
	Optional<Vendor> findByName(String name);
}
