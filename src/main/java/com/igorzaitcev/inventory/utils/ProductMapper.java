package com.igorzaitcev.inventory.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.igorzaitcev.inventory.dto.ProductDTO;
import com.igorzaitcev.inventory.model.UnitOfMeasure;

@Component
public class ProductMapper {
	private Random random = new Random();
	private String name;
	private List<ProductDTO> products = new ArrayList<>();

	public List<ProductDTO> createProducts() {
		for (int i = 0; i < 10; i++) {
			products.add(createProduct());
		}
		return products;

	}

	private ProductDTO createProduct() {
		ProductDTO product = new ProductDTO();
		product.setName(createName());
		product.setUnits(UnitOfMeasure.LITER);
		Integer cost = getRandom20();
		product.setCost(cost);
		product.setPrice((int) (cost * 1.3));
		return product;
	}

	private boolean containsName(String name) {
		return products.stream().map(ProductDTO::getName).anyMatch(p -> p.equals(name));
	}

	private String createName() {
		Faker faker = new Faker();
		name = faker.beer().name();
		if (containsName(name)) {
			System.out.println("Double name: " + name);
			createName();
		}
		return name;
	}

	private Integer getRandom20() {
		return random.nextInt(20) + 1;
	}

}
