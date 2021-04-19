package com.igorzaitcev.inventory.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.inventory.model.Customer;
import com.igorzaitcev.inventory.repository.CustomerRepository;

@Service
@Transactional
public class CustomerManagement {
	private static final Logger log = LoggerFactory.getLogger(CustomerManagement.class);

	private final CustomerRepository repository;

	public CustomerManagement(CustomerRepository repository) {
		this.repository = repository;
	}

	public Optional<Customer> findCustomer(Long id) {
		return repository.findById(id);
	}

	public List<Customer> findCustomers() {
		return repository.findAll();
	}

	public Customer addCustomer(Customer customer) {
		log.debug("Service add Customer: " + customer.getName());
		return repository.save(customer);
	}

	public long countCustomers() {
		return repository.count();
	}

	public void deleteCustomer(Long id) {
		log.debug("Service delete Customer: " + id);
		repository.deleteById(id);
	}

	public Optional<Customer> findByName(String name) {
		return repository.findByName(name);
	}

}
