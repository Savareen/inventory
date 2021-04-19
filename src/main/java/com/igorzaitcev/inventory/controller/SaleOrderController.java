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

import com.igorzaitcev.inventory.dto.SaleDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.SaleOrderManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/sales")
@Tag(name = "saleOrder", description = "the Sale Order API")
public class SaleOrderController {

	private SaleOrderManagement management;

	public SaleOrderController(SaleOrderManagement management) {
		this.management = management;
	}

	@Operation(summary = "Get a sale order by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the sale order", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = SaleDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Sale Order not found", content = @Content) })
	@GetMapping(path = "{id}")
	public SaleDTO getSaleOrder(@PathVariable("id") Long id) {
		try {
			return management.findSaleOrder(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	@Operation(summary = "Get all sale orders")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the sale orders", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SaleDTO.class)))) })
	@GetMapping(path = "/all")
	public List<SaleDTO> getAllSaleOrders() {
		return management.findSaleOrders();
	}

	@Operation(summary = "Delete a sale order by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "sale order deleted"),
			@ApiResponse(responseCode = "404", description = "Sale order cannot be deleted because of stock", content = @Content) })
	@DeleteMapping(path = "{id}")
	public void deleteSaleOrder(@PathVariable("id") Long id) {
		try {
			management.deleteSaleOrder(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	@Operation(summary = "Save a new sale order or update existing")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Sale order created"),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PostMapping(path = "/add")
	public void addSaleOrder(@Valid @RequestBody SaleDTO saleDTO) {
		management.addSaleOrder(saleDTO);
	}
}
