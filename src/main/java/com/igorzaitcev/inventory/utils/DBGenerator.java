package com.igorzaitcev.inventory.utils;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.igorzaitcev.inventory.dto.PurchaseDTO;
import com.igorzaitcev.inventory.dto.SaleDTO;
import com.igorzaitcev.inventory.model.Customer;
import com.igorzaitcev.inventory.model.Vendor;
import com.igorzaitcev.inventory.service.ProductManagement;

@Profile("!test")
@Component
public class DBGenerator {
	private ProductMapper productMapper;
	private ProductManagement productManagement;
	private VendorMapper vendorMapper;
	private PurchaseDTOMapperUtils purchaseDTOMapper;
	private SaleDTOMapperUtils saleDTOMapper;

	public DBGenerator(ProductMapper productMapper, ProductManagement productManagement, VendorMapper vendorMapper,
			PurchaseDTOMapperUtils purchaseDTOMapper, SaleDTOMapperUtils saleDTOMapper) {
		this.productMapper = productMapper;
		this.productManagement = productManagement;
		this.vendorMapper = vendorMapper;
		this.purchaseDTOMapper = purchaseDTOMapper;
		this.saleDTOMapper = saleDTOMapper;
	}

	public boolean isDatabaseEmpty() {
		return productManagement.countProducts() == 0L;
	}

	public void getProducts() {
		productMapper.createProducts().stream().forEach(productManagement::addProduct);
	}

	public List<Vendor> getVendors() {
		return vendorMapper.createVendors();
	}

	public List<Customer> getCustomers() {
		return vendorMapper.createCustomers();
	}

	public List<PurchaseDTO> getPurchaseDTOs() {
		return purchaseDTOMapper.createPurchaseDTOs();
	}

	public List<SaleDTO> getSaleDTOs() {
		return saleDTOMapper.createSaleDTOs();
	}
}
