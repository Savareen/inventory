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

import com.igorzaitcev.inventory.dto.PurchaseDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.PurchaseOrderManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/purchases")
@Tag(name = "purchaseOrder", description = "the Purchase Order API")
public class PurchaseOrderController {

	private PurchaseOrderManagement management;

	public PurchaseOrderController(PurchaseOrderManagement management) {
		this.management = management;
	}

	@Operation(summary = "Get a purchase order by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the purchase order", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Purchase Order not found", content = @Content) })
	@GetMapping(path = "{id}")
	public PurchaseDTO getPurchaseOrder(@PathVariable("id") Long id) {
		try {
			return management.findPurchaseOrder(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	@Operation(summary = "Get all purchase orders")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the purchase orders", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PurchaseDTO.class)))) })
	@GetMapping(path = "/all")
	public List<PurchaseDTO> getAllPurchaseOrders() {
		return management.findPurchaseOrders();
	}

	@Operation(summary = "Delete a purchase order by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "purchase order deleted"),
			@ApiResponse(responseCode = "404", description = "Purchase order cannot be deleted because of stock", content = @Content) })
	@DeleteMapping(path = "{id}")
	public void deletePurchaseOrder(@PathVariable("id") Long id) {
		try {
			management.deletePurchaseOrder(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	@Operation(summary = "Save a new purchase order or update existing")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Purchase order created"),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PostMapping(path = "/add")
	public void addPurchaseOrder(@Valid @RequestBody PurchaseDTO purchaseDTO) {
		management.addPurchaseOrder(purchaseDTO);
	}

}
