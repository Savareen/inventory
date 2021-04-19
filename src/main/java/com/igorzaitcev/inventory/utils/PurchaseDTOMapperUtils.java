package com.igorzaitcev.inventory.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.dto.PurchaseDTO;
import com.igorzaitcev.inventory.repository.ProductRepository;
import com.igorzaitcev.inventory.repository.VendorRepository;

@Component
public class PurchaseDTOMapperUtils {
	private Random random = new Random();
	private List<PurchaseDTO> purchaseDTOs = new ArrayList<>();
	private ProductRepository productRepository;
	private VendorRepository vendorRepository;

	public PurchaseDTOMapperUtils(ProductRepository productRepository, VendorRepository vendorRepository) {
		super();
		this.productRepository = productRepository;
		this.vendorRepository = vendorRepository;
	}

	public List<PurchaseDTO> createPurchaseDTOs() {
		for (int i = 0; i < 7; i++) {
			purchaseDTOs.add(createPurchaseDTO());
		}
		return purchaseDTOs;
	}

	private PurchaseDTO createPurchaseDTO() {
		Faker faker = new Faker();
		PurchaseDTO purchaseDTO = new PurchaseDTO();
		purchaseDTO.setCommitmentDate(LocalDate.now().minusDays(getRandom(7)));
		purchaseDTO.setScheduledDate(LocalDate.now().plusDays(getRandom(7)));
		purchaseDTO.setItems(createItemDTOs());
		purchaseDTO.setVendorName(vendorRepository.findById((long) getRandom(5)).get().getName());
		purchaseDTO.setOrderNumber(faker.phoneNumber().hashCode());
		return purchaseDTO;
	}

	private List<ItemDTO> createItemDTOs() {
		List<ItemDTO> items = new ArrayList<>();
		for (int i = 0; i < getRandom(5); i++) {
			items.add(createItemDTO());
		}
		return items;
	}

	private ItemDTO createItemDTO() {
		ItemDTO item = new ItemDTO();
		item.setQuantity(getRandom(20));
		item.setProductName(productRepository.findById((long) getRandom(10)).get().getName());
		return item;
	}

	private Integer getRandom(int max) {
		return random.nextInt(max) + 1;
	}
}
