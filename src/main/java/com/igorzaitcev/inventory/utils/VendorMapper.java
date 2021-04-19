package com.igorzaitcev.inventory.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.igorzaitcev.inventory.model.Customer;
import com.igorzaitcev.inventory.model.Vendor;

@Component
public class VendorMapper {
	private List<Vendor> vendors = new ArrayList<>();
	private List<Customer> customers = new ArrayList<>();
	private String delimeter = ", ";

	Faker faker = new Faker();

	public List<Vendor> createVendors() {
		for (int i = 0; i < 5; i++) {
			vendors.add(createVendor());
		}
		return vendors;
	}

	public List<Customer> createCustomers() {
		for (int i = 0; i < 5; i++) {
			customers.add(createCustomer());
		}
		return customers;
	}

	private Customer createCustomer() {
		Customer customer = new Customer();
		customer.setAddress(createAddress());
		customer.setName(faker.name().fullName());
		return customer;
	}

	private Vendor createVendor() {
		Vendor vendor = new Vendor();
		vendor.setName(createName());
		vendor.setAddress(createAddress());
		return vendor;
	}

	private String createAddress() {
		StringBuilder builder = new StringBuilder();
		builder.append(faker.address().buildingNumber());
		builder.append(delimeter);
		builder.append(faker.address().streetName());
		builder.append(delimeter);
		builder.append(faker.address().city());
		return builder.toString();
	}

	private String createName() {
		return faker.company().name();
	}
}
