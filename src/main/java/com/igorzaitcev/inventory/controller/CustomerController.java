package com.igorzaitcev.inventory.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.igorzaitcev.inventory.model.Customer;
import com.igorzaitcev.inventory.service.CustomerManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/customers")
@Tag(name = "customer", description = "the Customer API")
public class CustomerController {

	private CustomerManagement management;

	public CustomerController(CustomerManagement management) {
		this.management = management;
	}

	@Operation(summary = "Get a customer by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the customer", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content) })
	@GetMapping(path = "{id}")
	public Customer getCustomer(@PathVariable("id") Long id) {
		return isExist(id);
	}

	@Operation(summary = "Get all customers")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the customers", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))) })
	@GetMapping(path = "/all")
	public List<Customer> getAllCustomers() {
		return management.findCustomers();
	}

	@Operation(summary = "Save a new customer or updating exist")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Customer created"),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PostMapping(path = "/add")
	public void addCustomer(@Valid @RequestBody Customer customer) {
		management.addCustomer(customer);
	}

	@Operation(summary = "Delete a customer by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Customer deleted"),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content) })
	@DeleteMapping(path = "{id}")
	public void deleteCustomer(@PathVariable("id") Long id) {
		isExist(id);
		management.deleteCustomer(id);
	}

	private Customer isExist(Long id) {
		return management.findCustomer(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				String.format("No resource found for id (%s)", id)));
	}

}
