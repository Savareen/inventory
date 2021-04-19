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

import com.igorzaitcev.inventory.dto.DeliveryDTO;
import com.igorzaitcev.inventory.dto.ItemDTO;
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

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class DeliveryManagementTest {

	private static final Stock stock = new Stock(2L, 50, 30, 30,
			new Product(2L, "Product", UnitOfMeasure.LITER, 30, 30));
	private static final Product product = new Product(2L, "Product", UnitOfMeasure.LITER, 30, 30, stock);
	private static final SaleItem saleItem = new SaleItem(2L, 30, product);
	private static final ItemDTO itemDTO = new ItemDTO(2L, "Product", 30);
	private static final Customer customer = new Customer(2L, "Customer", "Address");
	private static final SaleOrder saleOrder = new SaleOrder(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), customer);
	private static final Delivery delivery = new Delivery(2L, saleOrder, false);
	private static final DeliveryDTO deliveryDTO = new DeliveryDTO(2L, false, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), "Customer", List.of(itemDTO));

	@Mock
	private DeliveryRepository repository;
	@Mock
	private SaleItemRepository itemRepository;

	@InjectMocks
	private DeliveryManagement management;

	@Test
	void shouldFindDelivery() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(delivery));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(saleItem));
		assertEquals(deliveryDTO, management.findDelivery(2L));
	}

	@Test
	void shouldThrowExceptionFind_WhenDeliveryNull() {
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.findDelivery(2L));
		assertEquals(String.format("No resource found for id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldFindActiveDeliveries() {
		when(repository.findByValidated(false)).thenReturn(List.of(delivery));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(saleItem));
		assertEquals(List.of(deliveryDTO), management.findActiveDeliveries());
	}

	@Test
	void shouldValidateDelivery() {
		Delivery deliveryFalse = new Delivery(2L, saleOrder, false);
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(deliveryFalse));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(saleItem));
		Delivery deliveryTrue = new Delivery(2L, saleOrder, true);
		management.validateDelivery(2L);
		verify(repository, times(1)).save(deliveryTrue);
	}
}
