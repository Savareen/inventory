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
import com.igorzaitcev.inventory.dto.PurchaseDTO;
import com.igorzaitcev.inventory.model.Product;
import com.igorzaitcev.inventory.model.PurchaseItem;
import com.igorzaitcev.inventory.model.PurchaseOrder;
import com.igorzaitcev.inventory.model.Receipt;
import com.igorzaitcev.inventory.model.Stock;
import com.igorzaitcev.inventory.model.UnitOfMeasure;
import com.igorzaitcev.inventory.model.Vendor;
import com.igorzaitcev.inventory.repository.ProductRepository;
import com.igorzaitcev.inventory.repository.PurchaseItemRepository;
import com.igorzaitcev.inventory.repository.ReceiptRepository;
import com.igorzaitcev.inventory.repository.VendorRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PurchaseDTOMapperTest {

	private static final Stock stock = new Stock(2L, 10, 20, 10,
			new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20));
	private static final Product product = new Product(2L, "Product", UnitOfMeasure.LITER, 10, 20, stock);
	private static final Receipt receipt = new Receipt(2L, false);
	private static final Vendor vendor = new Vendor(2L, "Vendor", "Address");
	private static final ItemDTO itemDTO = new ItemDTO(2L, "Product", 20);
	private static final PurchaseDTO purchaseDTO = new PurchaseDTO(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), "Vendor", 2L, List.of(itemDTO));
	private static final PurchaseOrder purchaseOrder = new PurchaseOrder(2L, 1001, LocalDate.parse("2021-04-07"),
			LocalDate.parse("2021-04-10"), vendor);
	private static final PurchaseItem purchaseItem = new PurchaseItem(2L, 20, product, purchaseOrder);

	@Mock
	private ProductRepository productRepository;
	@Mock
	private VendorRepository vendorRepository;
	@Mock
	private ReceiptRepository receiptRepository;
	@Mock
	private PurchaseItemRepository itemRepository;

	@InjectMocks
	private PurchaseDTOMapper mapper;

	@Test
	void shouldReturnPurchaseOrder() {
		when(vendorRepository.findByName("Vendor")).thenReturn(Optional.ofNullable(vendor));
		assertEquals(purchaseOrder, mapper.convertToPurchaseOrder(purchaseDTO));
	}

	@Test
	void shouldReturnPurchaseDTO() {
		when(receiptRepository.findByOrderId(2L)).thenReturn(Optional.ofNullable(receipt));
		when(itemRepository.findByOrderId(2L)).thenReturn(List.of(purchaseItem));
		assertEquals(purchaseDTO, mapper.convertToPurchaseDTO(purchaseOrder));
	}

	@Test
	void shouldReturnItemDTO() {
		assertEquals(itemDTO, mapper.convertToItemDTO(purchaseItem));
	}

	@Test
	void shouldReturnPurchaseItem() {
		when(productRepository.findByName("Product")).thenReturn(Optional.ofNullable(product));
		assertEquals(purchaseItem, mapper.convertToPurchaseItem(itemDTO, purchaseOrder));
	}
}
