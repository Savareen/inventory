package com.igorzaitcev.inventory.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.igorzaitcev.inventory.dto.StockRequestDTO;
import com.igorzaitcev.inventory.dto.StockResponseDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.StockManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/stocks")
@Tag(name = "stock", description = "the Stock API")
public class StockController {

	private StockManagement management;

	public StockController(StockManagement management) {
		this.management = management;
	}

	@Operation(summary = "Get a stock by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found the stock", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = StockRequestDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Stock not found", content = @Content) })
	@GetMapping(path = "{id}")
	public StockRequestDTO getStock(@PathVariable("id") Long id) {
		try {
			return management.findStock(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	@Operation(summary = "Get all stocks")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the stocks", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StockRequestDTO.class)))) })
	@GetMapping(path = "/all")
	public List<StockRequestDTO> getStocks() {
		return management.findStocks();
	}

	@Operation(summary = "Set initial stock")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Stock changed"),
			@ApiResponse(responseCode = "404", description = "Stock not found", content = @Content) })
	@PutMapping(path = "/initialize")
	public void validateDelivery(@Valid @RequestBody StockResponseDTO stockDTO) {
		try {
			management.setInitialStock(stockDTO);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

}
