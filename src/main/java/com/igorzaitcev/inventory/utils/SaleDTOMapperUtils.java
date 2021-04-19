package com.igorzaitcev.inventory.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.dto.SaleDTO;
import com.igorzaitcev.inventory.repository.CustomerRepository;
import com.igorzaitcev.inventory.repository.ProductRepository;

@Component
public class SaleDTOMapperUtils {
	private Random random = new Random();
	private List<SaleDTO> saleDTOs = new ArrayList<>();
	private ProductRepository productRepository;
	private CustomerRepository customerRepository;

	public SaleDTOMapperUtils(ProductRepository productRepository, CustomerRepository customerRepository) {
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
	}

	public List<SaleDTO> createSaleDTOs() {
		for (int i = 0; i < 7; i++) {
			saleDTOs.add(createSaleDTO());
		}
		return saleDTOs;
	}

	private SaleDTO createSaleDTO() {
		Faker faker = new Faker();
		SaleDTO saleDTO = new SaleDTO();
		saleDTO.setCommitmentDate(LocalDate.now().minusDays(getRandom(7)));
		saleDTO.setScheduledDate(LocalDate.now().plusDays(getRandom(7)));
		saleDTO.setItems(createItemDTOs());
		saleDTO.setCustomerName(customerRepository.findById((long) getRandom(5)).get().getName());
		saleDTO.setOrderNumber(faker.phoneNumber().hashCode());
		return saleDTO;
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
