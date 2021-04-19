package com.igorzaitcev.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.inventory.dto.DeliveryDTO;
import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Delivery;
import com.igorzaitcev.inventory.model.SaleItem;
import com.igorzaitcev.inventory.repository.DeliveryRepository;
import com.igorzaitcev.inventory.repository.SaleItemRepository;

@Service
@Transactional
public class DeliveryManagement {
	private static final Logger log = LoggerFactory.getLogger(DeliveryManagement.class);

	private final DeliveryRepository repository;
	private final SaleItemRepository itemRepository;

	public DeliveryManagement(DeliveryRepository repository, SaleItemRepository itemRepository) {
		this.repository = repository;
		this.itemRepository = itemRepository;
	}

	public DeliveryDTO findDelivery(Long id) {
		Optional<Delivery> delivery = repository.findById(id);
		if (delivery.isEmpty()) {
			log.debug("Service Delivery not found: " + id);
			throw new InvalidArgumentException(String.format("No resource found for id (%s)", id));
		}
		return convertToDeliveryDTO(delivery.get());
	}

	public List<DeliveryDTO> findActiveDeliveries() {
		return repository.findByValidated(false).stream().map(this::convertToDeliveryDTO).collect(Collectors.toList());
	}

	public void validateDelivery(Long id) {
		Delivery delivery = repository.findById(id).get();
		checkStock(delivery);
		delivery.setValidated(true);
		repository.save(delivery);
		log.debug("Service Delivery is validated: " + id);
		subFromStock(delivery);
	}

	private void checkStock(Delivery delivery) {
		List<SaleItem> items = getItems(delivery);
		if (items.stream().anyMatch(item -> (item.getProduct().getStock().getOnHand() - item.getQuantity() < 0))) {
			log.debug("Delivery cannot be validated: " + delivery.getId());
			throw new InvalidArgumentException(String.format("Delivery cannot be validated id (%s)", delivery.getId()));
		}

	}

	private void subFromStock(Delivery delivery) {
		getItems(delivery).stream().peek(this::subOnHand).forEach(this::removeFromOnSale);
	}

	private SaleItem subOnHand(SaleItem item) {
		item.getProduct().getStock().setOnHand(item.getProduct().getStock().getOnHand() - item.getQuantity());
		return item;
	}

	private SaleItem removeFromOnSale(SaleItem item) {
		item.getProduct().getStock().setOnSale(item.getProduct().getStock().getOnSale() - item.getQuantity());
		return item;
	}

	private DeliveryDTO convertToDeliveryDTO(Delivery delivery) {
		DeliveryDTO deliveryDTO = new DeliveryDTO();
		deliveryDTO.setCommitmentDate(delivery.getSaleOrder().getCommitmentDate());
		deliveryDTO.setScheduledDate(delivery.getSaleOrder().getScheduledDate());
		deliveryDTO.setDeliveryId(delivery.getId());
		deliveryDTO.setValidated(delivery.getValidated());
		deliveryDTO.setCustomerName(delivery.getSaleOrder().getCustomer().getName());
		deliveryDTO.setItems(getItems(delivery).stream().map(this::convertToItemDTO)
				.collect(Collectors.toList()));
		return deliveryDTO;
	}

	private ItemDTO convertToItemDTO(SaleItem saleItem) {
		ItemDTO item = new ItemDTO();
		item.setId(saleItem.getId());
		item.setProductName(saleItem.getProduct().getName());
		item.setQuantity(saleItem.getQuantity());
		return item;
	}

	private List<SaleItem> getItems(Delivery delivery) {
		return itemRepository.findByOrderId(delivery.getSaleOrder().getId());
	}
}
