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
import com.igorzaitcev.inventory.dto.PurchaseDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Product;
import com.igorzaitcev.inventory.model.PurchaseItem;
import com.igorzaitcev.inventory.model.PurchaseOrder;
import com.igorzaitcev.inventory.model.Receipt;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.model.Vendor;
import com.igorzaitcev.inventory.repository.PurchaseItemRepository;
import com.igorzaitcev.inventory.repository.PurchaseOrderRepository;
import com.igorzaitcev.inventory.repository.ReceiptRepository;
import com.igorzaitcev.inventory.service.mapper.PurchaseDTOMapper;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PurchaseOrderManagementTest {
	private static final Receipt receipt = new Receipt(2L, false);
	private static final ItemDTO itemDTO = new ItemDTO(2L, "Product", 20);
	private static final Stock stock = new Stock(2L, 10, 20, 10,
			new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20));
	private static final Vendor vendor = new Vendor(2L, "Vendor", "Address");
	private static final Product product = new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20, stock);
	private static final PurchaseItem purchaseItem = new PurchaseItem(2L, 20, product);
	private static final PurchaseDTO purchaseDTO = new PurchaseDTO(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), "Vendor", 2L, List.of(itemDTO));
	private static final PurchaseOrder purchaseOrder = new PurchaseOrder(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), vendor);

	@Mock
	private PurchaseOrderRepository repository;
	@Mock
	private PurchaseDTOMapper mapper;
	@Mock
	private ReceiptRepository receiptRepository;
	@Mock
	private PurchaseItemRepository itemRepository;

	@InjectMocks
	PurchaseOrderManagement management;

	@Test
	void shouldFindPurchaseOrder() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(purchaseOrder));
		when(mapper.convertToPurchaseDTO(purchaseOrder)).thenReturn(purchaseDTO);
		assertEquals(purchaseDTO, management.findPurchaseOrder(2L));
	}

	@Test
	void shouldThrowExceptionFind_WhenPurchaseOrderNull() {
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.findPurchaseOrder(2L));
		assertEquals(String.format("No resource found for id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldFindPurchaseOrders() {
		when(repository.findAll()).thenReturn(List.of(purchaseOrder));
		when(mapper.convertToPurchaseDTO(purchaseOrder)).thenReturn(purchaseDTO);
		assertEquals(purchaseDTO, management.findPurchaseOrders().get(0));
	}

	@Test
	void shouldDeletePurchaseOrder() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(purchaseOrder));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(purchaseItem));
		when(receiptRepository.findByOrderId(2L)).thenReturn(Optional.ofNullable(receipt));
		management.deletePurchaseOrder(2L);
		verify(repository, times(1)).deleteById(2L);
		verify(itemRepository, times(1)).deleteById(2L);
		verify(receiptRepository, times(1)).deleteById(2L);
	}

	@Test
	void shouldThrowExceptionDelete_WhenReceiptTrue() {
		Receipt receiptTrue = new Receipt(2L, true);
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(purchaseOrder));
		when(receiptRepository.findByOrderId(2L)).thenReturn(Optional.ofNullable(receiptTrue));
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.deletePurchaseOrder(2L));
		assertEquals(String.format("Order cannot be deleted id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldSavePurchaseOrder() {
		when(mapper.convertToPurchaseOrder(purchaseDTO)).thenReturn(purchaseOrder);
		when(repository.save(purchaseOrder)).thenReturn(purchaseOrder);
		when(mapper.convertToPurchaseItem(itemDTO, purchaseOrder)).thenReturn(purchaseItem);
		management.addPurchaseOrder(purchaseDTO);
		verify(repository, times(1)).save(purchaseOrder);
	}

}
