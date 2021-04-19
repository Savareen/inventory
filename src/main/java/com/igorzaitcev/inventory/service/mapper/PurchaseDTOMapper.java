package com.igorzaitcev.inventory.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.dto.PurchaseDTO;
import com.igorzaitcev.inventory.model.PurchaseItem;
import com.igorzaitcev.inventory.model.PurchaseOrder;
import com.igorzaitcev.inventory.model.Receipt;
import com.igorzaitcev.inventory.repository.ProductRepository;
import com.igorzaitcev.inventory.repository.PurchaseItemRepository;
import com.igorzaitcev.inventory.repository.ReceiptRepository;
import com.igorzaitcev.inventory.repository.VendorRepository;

@Component
public class PurchaseDTOMapper {

	private ProductRepository productRepository;
	private VendorRepository vendorRepository;
	private ReceiptRepository receiptRepository;
	private PurchaseItemRepository itemRepository;

	public PurchaseDTOMapper(ProductRepository productRepository, VendorRepository vendorRepository,
			ReceiptRepository receiptRepository, PurchaseItemRepository itemRepository) {
		this.productRepository = productRepository;
		this.vendorRepository = vendorRepository;
		this.receiptRepository = receiptRepository;
		this.itemRepository = itemRepository;
	}

	public PurchaseOrder convertToPurchaseOrder(PurchaseDTO purchaseDTO) {
		PurchaseOrder order = new PurchaseOrder();
		order.setId(purchaseDTO.getOrderId());
		order.setVendor(vendorRepository.findByName(purchaseDTO.getVendorName()).get());
		order.setNumber(purchaseDTO.getOrderNumber());
		order.setCommitmentDate(purchaseDTO.getCommitmentDate());
		order.setScheduledDate(purchaseDTO.getScheduledDate());
		return order;
	}

	public PurchaseDTO convertToPurchaseDTO(PurchaseOrder order) {
		PurchaseDTO purchaseDTO = new PurchaseDTO();
		purchaseDTO.setOrderId(order.getId());
		purchaseDTO.setOrderNumber(order.getNumber());
		purchaseDTO.setCommitmentDate(order.getCommitmentDate());
		purchaseDTO.setScheduledDate(order.getScheduledDate());
		purchaseDTO.setVendorName(order.getVendor().getName());
		purchaseDTO.setReceiptId(findReceipt(order.getId()).getId());
		List<ItemDTO> items = findPurchaseItems(order.getId()).stream().map(this::convertToItemDTO)
				.collect(Collectors.toList());
		purchaseDTO.setItems(items);
		return purchaseDTO;

	}

	public ItemDTO convertToItemDTO(PurchaseItem purchaseItem) {
		ItemDTO item = new ItemDTO();
		item.setId(purchaseItem.getId());
		item.setProductName(purchaseItem.getProduct().getName());
		item.setQuantity(purchaseItem.getQuantity());
		return item;
	}

	public PurchaseItem convertToPurchaseItem(ItemDTO itemDTO, PurchaseOrder order) {
		PurchaseItem item = new PurchaseItem();
		item.setId(itemDTO.getId());
		item.setProduct(productRepository.findByName(itemDTO.getProductName()).get());
		item.setQuantity(itemDTO.getQuantity());
		item.setPurchaseOrder(order);
		return item;
	}

	private List<PurchaseItem> findPurchaseItems(Long orderId) {
		return itemRepository.findByOrderId(orderId);
	}

	private Receipt findReceipt(Long id) {
		return receiptRepository.findByOrderId(id).get();
	}

}
