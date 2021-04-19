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

import com.igorzaitcev.inventory.dto.ProductDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.service.ProductManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "product", description = "the Product API")
public class ProductController {

	private ProductManagement management;

	public ProductController(ProductManagement management) {
		this.management = management;
	}

	@Operation(summary = "Get a product by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the product", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Product not found", content = @Content) })
	@GetMapping(path = "{id}")
	public ProductDTO getProduct(@PathVariable("id") Long id) {
		try {
			return management.findProduct(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	@Operation(summary = "Get all products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the products", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))) })
	@GetMapping(path = "/all")
	public List<ProductDTO> getAllProducts() {
		return management.findProducts();
	}

	@Operation(summary = "Save a new product or update existing")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Product created"),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PostMapping(path = "/add")
	public void addProduct(@Valid @RequestBody ProductDTO productDTO) {
		try {
			management.addProduct(productDTO);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}

	@Operation(summary = "Delete a product by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Product deleted"),
			@ApiResponse(responseCode = "400", description = "Product cannot be deleted because of stock", content = @Content) })
	@DeleteMapping(path = "{id}")
	public void deleteProduct(@PathVariable("id") Long id) {
		try {
			management.deleteProduct(id);
		} catch (InvalidArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
	}
}
