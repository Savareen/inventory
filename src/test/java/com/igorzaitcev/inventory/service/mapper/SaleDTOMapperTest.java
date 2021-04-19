package com.igorzaitcev.inventory.service.mapper;

import static org.junit.Assert.assertEquals;
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
import com.igorzaitcev.inventory.model.Customer;
import com.igorzaitcev.inventory.model.Delivery;
import com.igorzaitcev.inventory.model.Product;
import com.igorzaitcev.inventory.model.SaleItem;
import com.igorzaitcev.inventory.model.SaleOrder;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.repository.CustomerRepository;
import com.igorzaitcev.inventory.repository.DeliveryRepository;
import com.igorzaitcev.inventory.repository.ProductRepository;
import com.igorzaitcev.inventory.repository.SaleItemRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SaleDTOMapperTest {

	private static final Delivery delivery = new Delivery(2L, false);
	private static final ItemDTO itemDTO = new ItemDTO(2L, "Product", 20);
	private static final Stock stock = new Stock(2L, 10, 20, 10,
			new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20));
	private static final Customer customer = new Customer(2L, "Customer", "Address");
	private static final Product product = new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20, stock);
	private static final SaleDTO saleDTO = new SaleDTO(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), "Customer", 2L, List.of(itemDTO));
	private static final SaleOrder saleOrder = new SaleOrder(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), customer);
	private static final SaleItem saleItem = new SaleItem(2L, 20, product, saleOrder);

	@Mock
	private ProductRepository productRepository;
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private DeliveryRepository deliveryRepository;
	@Mock
	private SaleItemRepository itemRepository;

	@InjectMocks
	private SaleDTOMapper mapper;

	@Test
	void shouldReturnSaleOrder() {
		when(customerRepository.findByName("Customer")).thenReturn(Optional.ofNullable(customer));
		assertEquals(saleOrder, mapper.convertToSaleOrder(saleDTO));
	}

	@Test
	void shouldReturnSaleDTO() {
		when(deliveryRepository.findByOrderId(2L)).thenReturn(Optional.ofNullable(delivery));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(saleItem));
		assertEquals(saleDTO, mapper.convertToSaleDTO(saleOrder));
	}

	@Test
	void shouldReturnItemDTO() {
		assertEquals(itemDTO, mapper.convertToItemDTO(saleItem));
	}

	@Test
	void shouldReturnSaleItem() {
		when(productRepository.findByName("Product")).thenReturn(Optional.ofNullable(product));
		assertEquals(saleItem, mapper.convertToSaleItem(itemDTO, saleOrder));
	}

}
