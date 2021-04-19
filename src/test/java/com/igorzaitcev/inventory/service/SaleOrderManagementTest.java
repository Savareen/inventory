package com.igorzaitcev.inventory.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.dto.SaleDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Customer;
import com.igorzaitcev.inventory.model.Delivery;
import com.igorzaitcev.inventory.model.Product;
import com.igorzaitcev.inventory.model.SaleItem;
import com.igorzaitcev.inventory.model.SaleOrder;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.repository.DeliveryRepository;
import com.igorzaitcev.inventory.repository.SaleItemRepository;
import com.igorzaitcev.inventory.repository.SaleOrderRepository;
import com.igorzaitcev.inventory.service.mapper.SaleDTOMapper;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SaleOrderManagementTest {
	private static final Delivery delivery = new Delivery(2L, false);
	private static final ItemDTO itemDTO = new ItemDTO(2L, "Product", 20);
	private static final Stock stock = new Stock(2L, 10, 20, 10,
			new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20));
	private static final Customer customer = new Customer(2L, "Customer", "Address");
	private static final Product product = new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20, stock);
	private static final SaleItem saleItem = new SaleItem(2L, 20, product);
	private static final SaleDTO saleDTO = new SaleDTO(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), "Customer", 2L, List.of(itemDTO));
	private static final SaleOrder saleOrder = new SaleOrder(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), customer);

	@Mock
	private SaleOrderRepository repository;
	@Mock
	private SaleDTOMapper mapper;
	@Mock
	private DeliveryRepository deliveryRepository;
	@Mock
	private SaleItemRepository itemRepository;

	@InjectMocks
	private SaleOrderManagement management;

	@Test
	void shouldFindSaleOrder() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(saleOrder));
		when(mapper.convertToSaleDTO(saleOrder)).thenReturn(saleDTO);
		assertEquals(saleDTO, management.findSaleOrder(2L));
	}

	@Test
	void shouldThrowExceptionFind_WhenSaleOrderNull() {
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.findSaleOrder(2L));
		assertEquals(String.format("No resource found for id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldFindSaleOrders() {
		when(repository.findAll()).thenReturn(List.of(saleOrder));
		when(mapper.convertToSaleDTO(saleOrder)).thenReturn(saleDTO);
		assertEquals(saleDTO, management.findSaleOrders().get(0));
	}

	@Test
	void shouldDeleteSaleOrder() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(saleOrder));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(saleItem));
		when(deliveryRepository.findByOrderId(2L)).thenReturn(Optional.ofNullable(delivery));
		management.deleteSaleOrder(2L);
		verify(repository, times(1)).deleteById(2L);
		verify(itemRepository, times(1)).deleteById(2L);
		verify(deliveryRepository, times(1)).deleteById(2L);
	}

	@Test
	void shouldThrowExceptionDelete_WhenDeliveryTrue() {
		Delivery deliveryTrue = new Delivery(2L, true);
		when(deliveryRepository.findByOrderId(2L)).thenReturn(Optional.ofNullable(deliveryTrue));
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(saleOrder));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deleteSaleOrder(2L));
		assertEquals(String.format("Order cannot be deleted id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldSavePurchaseOrder() {
		when(mapper.convertToSaleOrder(saleDTO)).thenReturn(saleOrder);
		when(repository.save(saleOrder)).thenReturn(saleOrder);
		when(mapper.convertToSaleItem(itemDTO, saleOrder)).thenReturn(saleItem);
		management.addSaleOrder(saleDTO);
		verify(repository, times(1)).save(saleOrder);
	}

}
