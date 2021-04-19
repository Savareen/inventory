package com.igorzaitcev.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.igorzaitcev.inventory.dto.ReceiptDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.ReceiptManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/receipts")
@Tag(name = "receipt", description = "the Receipt API")
public class ReceiptController {

	private ReceiptManagement management;

	public ReceiptController(ReceiptManagement management) {
		this.management = management;
	}

	@Operation(summary = "Get a receipt by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the receipt", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ReceiptDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Receipt not found", content = @Content) })
	@GetMapping(path = "{id}")
	public ReceiptDTO getReceipt(@PathVariable("id") Long id) {
		try {
			return management.findReceipt(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	@Operation(summary = "Get all receipts")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the receipts", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReceiptDTO.class)))) })
	@GetMapping(path = "/all")
	public List<ReceiptDTO> getActiveReceipts() {
		return management.findActiveReceipts();
	}

	@Operation(summary = "Validate receipt")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Receipt validated"),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PutMapping(path = "{id}")
	public void validateReceipt(@PathVariable("id") Long id) {
		management.validateReceipt(id);
	}

}
