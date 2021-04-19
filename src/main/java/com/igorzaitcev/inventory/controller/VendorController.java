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

import com.igorzaitcev.inventory.model.Vendor;
import com.igorzaitcev.inventory.service.VendorManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/vendors")
@Tag(name = "vendor", description = "the Vendor API")
public class VendorController {

	private VendorManagement management;

	public VendorController(VendorManagement management) {
		this.management = management;
	}

	@Operation(summary = "Get a vendor by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the vendor", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Vendor.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content) })
	@GetMapping(path = "{id}")
	public Vendor getVendor(@PathVariable("id") Long id) {
		return isExist(id);
	}

	@Operation(summary = "Get all vendors")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the vendors", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Vendor.class)))) })
	@GetMapping(path = "/all")
	public List<Vendor> getAllVendors() {
		return management.findVendors();
	}

	@Operation(summary = "Save a new vendor or update existing")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Vendor created"),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PostMapping(path = "/add")
	public void addVendor(@Valid @RequestBody Vendor vendor) {
		management.addVendor(vendor);
	}

	@Operation(summary = "Delete a vendor by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Vendor deleted"),
			@ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content) })
	@DeleteMapping(path = "{id}")
	public void deleteVendor(@PathVariable("id") Long id) {
		isExist(id);
		management.deleteVendor(id);
	}

	private Vendor isExist(Long id) {
		return management.findVendor(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				String.format("No resource found for id (%s)", id)));
	}

}
