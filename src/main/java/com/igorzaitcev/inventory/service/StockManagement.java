package com.igorzaitcev.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.inventory.dto.StockRequestDTO;
import com.igorzaitcev.inventory.dto.StockResponseDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.repository.StockRepository;

@Service
@Transactional
public class StockManagement {
	private static final Logger log = LoggerFactory.getLogger(StockManagement.class);

	private final StockRepository repository;

	public StockManagement(StockRepository repository) {
		this.repository = repository;
	}

	public StockRequestDTO findStock(Long id) {
		Optional<Stock> stock = repository.findById(id);
		if (stock.isEmpty()) {
			log.debug("Service Stock not found: " + id);
			throw new InvalidArgumentException(String.format("No resource found for id (%s)", id));
		}
		return convertToStockDTO(stock.get());
	}

	public List<StockRequestDTO> findStocks() {
		return repository.findAll().stream().map(this::convertToStockDTO).collect(Collectors.toList());
	}

	public void setInitialStock(StockResponseDTO stockDTO) {
		Optional<Stock> stockOptional = repository.findById(stockDTO.getStockId());
		if (stockOptional.isEmpty()) {
			log.debug("Service Stock not found: " + stockDTO.getStockId());
			throw new InvalidArgumentException(String.format("No resource found for id (%s)", stockDTO.getStockId()));
		}
		Stock stock = stockOptional.get();
		stock.setOnHand(stockDTO.getOnHand());
		log.debug("Service Stock set initial quantity: " + stockDTO.getStockId());
		repository.save(stock);
	}

	private StockRequestDTO convertToStockDTO(Stock stock) {
		StockRequestDTO stockDTO = new StockRequestDTO();
		stockDTO.setStockId(stock.getId());
		stockDTO.setProductName(stock.getProduct().getName());
		stockDTO.setUnits(stock.getProduct().getUnits());
		stockDTO.setOnHand(stock.getOnHand());
		stockDTO.setForecasted(stock.getForecasted());
		stockDTO.setOnSale(stock.getOnSale());
		return stockDTO;
	}
}
