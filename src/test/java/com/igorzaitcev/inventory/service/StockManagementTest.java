package com.igorzaitcev.inventory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.igorzaitcev.inventory.dto.StockRequestDTO;
import com.igorzaitcev.inventory.dto.StockResponseDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Product;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.repository.StockRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class StockManagementTest {

	private static final StockRequestDTO requestDTO = new StockRequestDTO(2L, "Product", UnitOfMeasure.LITER, 10, 20,
			10);
	private static final StockResponseDTO responseDTO = new StockResponseDTO(2L, 20);
	private static final Stock expectedStock = new Stock(2L, 10, 20, 10,
			new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20));

	@Mock
	StockRepository repository;

	@InjectMocks
	StockManagement management;

	@Test
	void shouldFindStock() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(expectedStock));
		assertEquals(requestDTO, management.findStock(2L));
	}

	@Test
	void shouldThrowException_WhenStockNull() {
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.findStock(2L));
		assertEquals(String.format("No resource found for id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldFindStocks() {
		when(repository.findAll()).thenReturn(List.of(expectedStock));
		assertEquals(requestDTO, management.findStocks().get(0));
	}

	@Test
	void shouldThrowException_WhenInitializeStockNull() {
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.setInitialStock(responseDTO));
		assertEquals(String.format("No resource found for id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldSaveStock() {
		Stock stock = new Stock(2L, 10, 20, 10, new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20));
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(stock));
		management.setInitialStock(responseDTO);

		ArgumentCaptor<Stock> stockCaptor = ArgumentCaptor.forClass(Stock.class);
		verify(repository, times(1)).save(stockCaptor.capture());
		assertThat(stockCaptor.getValue().getId()).isEqualTo(2L);
		assertThat(stockCaptor.getValue().getOnHand()).isEqualTo(20);
	}
}
