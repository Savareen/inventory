package com.igorzaitcev.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.dto.ReceiptDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.PurchaseItem;
import com.igorzaitcev.inventory.model.Receipt;
import com.igorzaitcev.inventory.repository.PurchaseItemRepository;
import com.igorzaitcev.inventory.repository.ReceiptRepository;

@Service
@Transactional
public class ReceiptManagement {
	private static final Logger log = LoggerFactory.getLogger(ReceiptManagement.class);

	private final ReceiptRepository repository;
	private final PurchaseItemRepository itemRepository;

	public ReceiptManagement(ReceiptRepository repository, PurchaseItemRepository itemRepository) {
		this.repository = repository;
		this.itemRepository = itemRepository;
	}

	public ReceiptDTO findReceipt(Long id) {
		Optional<Receipt> receipt = repository.findById(id);
		if (receipt.isEmpty()) {
			log.debug("Service Receipt not found: " + id);
			throw new InvalidArgumentException(String.format("No resource found for id (%s)", id));
		}
		return convertToReceiptDTO(receipt.get());
	}

	public List<ReceiptDTO> findActiveReceipts() {
		return repository.findByValidated(false).stream().map(this::convertToReceiptDTO).collect(Collectors.toList());
	}

	public void validateReceipt(Long id) {
		Receipt receipt = repository.findById(id).get();
		receipt.setValidated(true);
		repository.save(receipt);
		log.debug("Service Receipt is validated: " + id);
		addToStock(receipt);
	}

	private void addToStock(Receipt receipt) {
		getItems(receipt).stream().peek(this::addOnHand).forEach(this::removeFromForecasted);
	}

	private PurchaseItem addOnHand(PurchaseItem item) {
		item.getProduct().getStock().setOnHand(item.getProduct().getStock().getOnHand() + item.getQuantity());
		return item;
	}

	private PurchaseItem removeFromForecasted(PurchaseItem item) {
		item.getProduct().getStock().setForecasted(item.getProduct().getStock().getForecasted() - item.getQuantity());
		return item;
	}

	private ReceiptDTO convertToReceiptDTO(Receipt receipt) {
		ReceiptDTO receiptDTO = new ReceiptDTO();
		receiptDTO.setCommitmentDate(receipt.getPurchaseOrder().getCommitmentDate());
		receiptDTO.setScheduledDate(receipt.getPurchaseOrder().getScheduledDate());
		receiptDTO.setReceiptId(receipt.getId());
		receiptDTO.setValidated(receipt.getValidated());
		receiptDTO.setVendorName(receipt.getPurchaseOrder().getVendor().getName());
		receiptDTO.setItems(getItems(receipt).stream().map(this::convertToItemDTO)
				.collect(Collectors.toList()));
		return receiptDTO;
	}

	private ItemDTO convertToItemDTO(PurchaseItem purchaseItem) {
		ItemDTO item = new ItemDTO();
		item.setId(purchaseItem.getId());
		item.setProductName(purchaseItem.getProduct().getName());
		item.setQuantity(purchaseItem.getQuantity());
		return item;
	}

	private List<PurchaseItem> getItems(Receipt receipt) {
		return itemRepository.findByOrderId(receipt.getPurchaseOrder().getId());
	}
}
