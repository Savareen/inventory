package com.igorzaitcev.inventory.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.igorzaitcev.inventory.service.CustomerManagement;
import com.igorzaitcev.inventory.service.PurchaseOrderManagement;
import com.igorzaitcev.inventory.service.SaleOrderManagement;
import com.igorzaitcev.inventory.service.VendorManagement;

@Profile("!test")
@Service
public class DBRegistrator {

	private static DBGenerator generator;
	private static VendorManagement vendorManagement;
	private static CustomerManagement customerManagement;
	private static PurchaseOrderManagement purchaseOrder;
	private static SaleOrderManagement saleOrder;

	public DBRegistrator(DBGenerator generator, VendorManagement vendorManagement,
			CustomerManagement customerManagement, PurchaseOrderManagement purchaseOrder,
			SaleOrderManagement saleOrder) {
		this.generator = generator;
		this.vendorManagement = vendorManagement;
		this.customerManagement = customerManagement;
		this.purchaseOrder = purchaseOrder;
		this.saleOrder = saleOrder;
	}

	public static void populateDB() {
		if (generator.isDatabaseEmpty()) {
			generator.getProducts();
			generator.getVendors().stream().forEach(vendorManagement::addVendor);
			generator.getCustomers().stream().forEach(customerManagement::addCustomer);
			generator.getPurchaseDTOs().stream().forEach(purchaseOrder::addPurchaseOrder);
			generator.getSaleDTOs().stream().forEach(saleOrder::addSaleOrder);
		}
	}

}
