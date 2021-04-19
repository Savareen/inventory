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
import com.igorzaitcev.inventory.dto.ReceiptDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Product;
import com.igorzaitcev.inventory.model.PurchaseItem;
import com.igorzaitcev.inventory.model.PurchaseOrder;
import com.igorzaitcev.inventory.model.Receipt;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.model.Vendor;
import com.igorzaitcev.inventory.repository.PurchaseItemRepository;
import com.igorzaitcev.inventory.repository.ReceiptRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ReceiptManagementTest {

	private static final Stock stock = new Stock(2L, 10, 20, 10,
			new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20));
	private static final Product product = new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20, stock);
	private static final PurchaseItem purchaseItem = new PurchaseItem(2L, 20, product);
	private static final ItemDTO itemDTO = new ItemDTO(2L, "Product", 20);
	private static final Vendor vendor = new Vendor(2L, "Vendor", "Address");
	private static final PurchaseOrder purchaseOrder = new PurchaseOrder(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), vendor);
	private static final Receipt receipt = new Receipt(2L, purchaseOrder, false);
	private static final ReceiptDTO receiptDTO = new ReceiptDTO(2L, false, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), "Vendor", List.of(itemDTO));

	@Mock
	private ReceiptRepository repository;
	@Mock
	private PurchaseItemRepository itemRepository;

	@InjectMocks
	private ReceiptManagement management;

	@Test
	void shouldFindReceipt() {
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(receipt));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(purchaseItem));
		assertEquals(receiptDTO, management.findReceipt(2L));
	}

	@Test
	void shouldThrowExceptionFind_WhenReceiptNull() {
		InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
				() -> management.findReceipt(2L));
		assertEquals(String.format("No resource found for id (%s)", 2L), exception.getMessage());
	}

	@Test
	void shouldFindActiveReceipts() {
		when(repository.findByValidated(false)).thenReturn(List.of(receipt));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(purchaseItem));
		assertEquals(List.of(receiptDTO), management.findActiveReceipts());
	}

	@Test
	void shouldValidateReceipt() {
		Receipt receiptFalse = new Receipt(2L, purchaseOrder, false);
		when(repository.findById(2L)).thenReturn(Optional.ofNullable(receiptFalse));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(purchaseItem));
		Receipt receiptTrue = new Receipt(2L, purchaseOrder, true);
		management.validateReceipt(2L);
		verify(repository, times(1)).save(receiptTrue);
	}

}
