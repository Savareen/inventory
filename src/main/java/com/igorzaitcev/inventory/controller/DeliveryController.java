package com.igorzaitcev.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.igorzaitcev.inventory.dto.DeliveryDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.DeliveryManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/deliveries")
@Tag(name = "delivery", description = "the Delivery API")
public class DeliveryController {

	private DeliveryManagement management;

	public DeliveryController(DeliveryManagement management) {
		this.management = management;
	}

	@Operation(summary = "Get a delivery by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the delivery", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Delivery not found", content = @Content) })
	@GetMapping(path = "{id}")
	public DeliveryDTO getDelivery(@PathVariable("id") Long id) {
		try {
			return management.findDelivery(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	@Operation(summary = "Get all deliveries")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the deliveries", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DeliveryDTO.class)))) })
	@GetMapping(path = "/all")
	public List<DeliveryDTO> getActiveReceipts() {
		return management.findActiveDeliveries();
	}

	@Operation(summary = "Validate delivery")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Delivery validated"),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PutMapping(path = "{id}")
	public void validateDelivery(@PathVariable("id") Long id) {
		try {
			management.validateDelivery(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}

	}

}
