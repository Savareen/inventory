package com.igorzaitcev.inventory.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.inventory.model.Vendor;
import com.igorzaitcev.inventory.repository.VendorRepository;

@Service
@Transactional
public class VendorManagement {
	private static final Logger log = LoggerFactory.getLogger(VendorManagement.class);

	private final VendorRepository repository;

	public VendorManagement(VendorRepository repository) {
		this.repository = repository;
	}

	public Optional<Vendor> findVendor(Long id) {
		return repository.findById(id);
	}

	public List<Vendor> findVendors() {
		return repository.findAll();
	}

	public Vendor addVendor(Vendor vendor) {
		log.debug("Service add Vendor: " + vendor.getName());
		return repository.save(vendor);
	}

	public long countVendors() {
		return repository.count();
	}

	public void deleteVendor(Long id) {
		log.debug("Service delete Vendor: " + id);
		repository.deleteById(id);
	}

	public Optional<Vendor> findByName(String name) {
		return repository.findByName(name);
	}

}
