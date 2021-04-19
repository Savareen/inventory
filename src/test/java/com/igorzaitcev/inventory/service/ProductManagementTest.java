package com.igorzaitcev.inventory.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.igorzaitcev.inventory.dto.ProductDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Product;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.repository.ProductRepository;
import com.igorzaitcev.inventory.repository.StockRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductManagementTest {

	private static final Stock stock = new Stock(2L, 10, 20, 10,
			new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20));
	private static final Product product = new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20, stock);
	private static final ProductDTO productDTO = new ProductDTO(2L, "Product", UnitOfMeasure.LITER, 10, 20);

	@Mock
	private ProductRepository repository;
	@Mock
	private StockRepository stockRepository;

	@InjectMocks
	private ProductManagement management;

	@Test
	void shouldFindProduct() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(product));
		assertEquals(productDTO, management.findProduct(2L));
	}

	@Test
	void shouldThrowExceptionFind_WhenProductNull() {
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.findProduct(2L));
		assertEquals(String.format("No resource found for id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldFindProducts() {
		when(repository.findAll()).thenReturn(List.of(product));
		assertEquals(List.of(productDTO), management.findProducts());

	}

	@Test
	void shouldAddProduct() {
		management.addProduct(productDTO);
		verify(stockRepository, times(1)).save(new Stock(0, 0, 0, product));
	}

	@Test
	void shouldThrowExceptionFind_WhenProductInDB() {
		when(repository.findAll()).thenReturn(List.of(product));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.addProduct(productDTO));
		assertEquals(String.format("Product cannot be save name (%s)", "Product"), exception.getMessage());
	}

	@Test
	void shouldThrowExceptionFind_WhenProductInStock() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(product));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteProduct(2L));
		assertEquals(String.format("Product cannot be deleted from Stock id (%s)", 2L), exception.getMessage());
	}

}
