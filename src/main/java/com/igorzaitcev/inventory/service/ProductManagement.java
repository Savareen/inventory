package com.igorzaitcev.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.inventory.dto.ProductDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Product;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.repository.ProductRepository;
import com.igorzaitcev.inventory.repository.StockRepository;

@Service
@Transactional
public class ProductManagement {
	private static final Logger log = LoggerFactory.getLogger(ProductManagement.class);

	private final ProductRepository repository;
	private final StockRepository stockRepository;

	public ProductManagement(ProductRepository repository, StockRepository stockRepository) {
		this.repository = repository;
		this.stockRepository = stockRepository;
	}

	public ProductDTO findProduct(Long id) {
		Optional<Product> product = repository.findById(id);
		if (product.isEmpty()) {
			log.debug("Service Product not found: " + id);
			throw new InvalidArgumentException(String.format("No resource found for id (%s)", id));
		}
		return convertToProductDTO(product.get());
	}

	public List<ProductDTO> findProducts() {
		return repository.findAll().stream().map(this::convertToProductDTO).collect(Collectors.toList());
	}

	public void addProduct(ProductDTO productDTO) {
		if (repository.findAll().stream().anyMatch(p -> p.getName().equals(productDTO.getName()))) {
			log.debug("Service Product cannot be save name: " + productDTO.getName());
			throw new InvalidArgumentException(String.format("Product cannot be save name (%s)", productDTO.getName()));
		}
		Product product = convertToProduct(productDTO);
		stockRepository.save(new Stock(0, 0, 0, product));
	}

	public long countProducts() {
		return repository.count();
	}

	public void deleteProduct(Long id) {
		Stock stock = repository.findById(id).get().getStock();
		if (stock.getOnHand() > 0 || stock.getOnSale() > 0 || stock.getOnSale() > 0) {
			log.debug("Service Product cannot be deleted: " + id);
			throw new InvalidArgumentException(String.format("Product cannot be deleted from Stock id (%s)", id));
		}
		log.debug("Service delete Stock: " + stock.getId());
		stockRepository.deleteById(stock.getId());
		log.debug("Service delete Product: " + id);
		repository.deleteById(id);
	}

	public Optional<Product> findByName(String name) {
		return repository.findByName(name);
	}

	private ProductDTO convertToProductDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductId(product.getId());
		productDTO.setCost(product.getCost());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setUnits(product.getUnits());
		return productDTO;
	}

	private Product convertToProduct(ProductDTO productDTO) {
		Product product = new Product();
		product.setId(productDTO.getProductId());
		product.setCost(productDTO.getCost());
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setUnits(productDTO.getUnits());
		return product;
	}

}
