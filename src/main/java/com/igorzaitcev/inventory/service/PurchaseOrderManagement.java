package com.igorzaitcev.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.inventory.dto.PurchaseDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.PurchaseItem;
import com.igorzaitcev.inventory.model.PurchaseOrder;
import com.igorzaitcev.inventory.model.Receipt;
import com.igorzaitcev.inventory.repository.PurchaseItemRepository;
import com.igorzaitcev.inventory.repository.PurchaseOrderRepository;
import com.igorzaitcev.inventory.repository.ReceiptRepository;
import com.igorzaitcev.inventory.service.mapper.PurchaseDTOMapper;

@Service
@Transactional
public class PurchaseOrderManagement {
	private static final Logger log = LoggerFactory.getLogger(PurchaseOrderManagement.class);

	private PurchaseOrderRepository repository;
	private ReceiptRepository receiptRepository;
	private PurchaseItemRepository itemRepository;
	private PurchaseDTOMapper mapper;

	public PurchaseOrderManagement(PurchaseOrderRepository repository, ReceiptRepository receiptRepository,
			PurchaseItemRepository itemRepository, PurchaseDTOMapper mapper) {
		super();
		this.repository = repository;
		this.receiptRepository = receiptRepository;
		this.itemRepository = itemRepository;
		this.mapper = mapper;
	}


	public void addPurchaseOrder(PurchaseDTO purchaseDTO) {
		PurchaseOrder order = repository.save(mapper.convertToPurchaseOrder(purchaseDTO));
		log.debug("Service Purchase Order added: " + order.getId());
		saveReceipt(purchaseDTO, order);
		log.debug("Service Receipt added: " + order.getId());
		List<PurchaseItem> items = purchaseDTO.getItems().stream()
				.map(item -> mapper.convertToPurchaseItem(item, order)).peek(itemRepository::save)
				.collect(Collectors.toList());
		addToStock(items);
	}


	public List<PurchaseDTO> findPurchaseOrders() {
		return repository.findAll().stream().map(mapper::convertToPurchaseDTO).collect(Collectors.toList());
	}

	public PurchaseDTO findPurchaseOrder(Long id) {
		Optional<PurchaseOrder> order = repository.findById(id);
		if (order.isEmpty()) {
			log.debug("Service Purchase Order not found: " + id);
			throw new InvalidArgumentException(String.format("No resource found for id (%s)", id));
		}
		return mapper.convertToPurchaseDTO(order.get());
	}

	public void deletePurchaseOrder(Long id) {
		PurchaseOrder order = repository.findById(id).get();
		if (findReceipt(id).getValidated()) {
			log.debug("Service Purchase Order cannot be deleted: " + id);
			throw new InvalidArgumentException(String.format("Order cannot be deleted id (%s)", id));
		}
		removeFromStock(order);
		findPurchaseItems(id).stream().forEach(item -> itemRepository.deleteById(item.getId()));
		log.debug("Service Receipt deleted: " + id);
		receiptRepository.deleteById(findReceipt(id).getId());
		log.debug("Service Purchase Order deleted: " + id);
		repository.deleteById(id);
	}


	private void removeFromStock(PurchaseOrder order) {
		findPurchaseItems(order.getId()).stream().forEach(item -> item.getProduct().getStock()
				.setForecasted(item.getProduct().getStock().getForecasted() - item.getQuantity()));

	}

	private void addToStock(List<PurchaseItem> items) {
		items.stream().forEach(item -> item.getProduct().getStock()
				.setForecasted(item.getProduct().getStock().getForecasted() + item.getQuantity()));
	}

	private void saveReceipt(PurchaseDTO purchaseDTO, PurchaseOrder order) {
		if (purchaseDTO.getReceiptId() != null) {
			receiptRepository.findById(purchaseDTO.getReceiptId()).ifPresent(item -> item.setPurchaseOrder(order));
		} else {
			receiptRepository.save(new Receipt(order, false));
		}
	}

	private List<PurchaseItem> findPurchaseItems(Long orderId) {
		return itemRepository.findByOrderId(orderId);
	}

	private Receipt findReceipt(Long id) {
		return receiptRepository.findByOrderId(id).get();
	}
}
