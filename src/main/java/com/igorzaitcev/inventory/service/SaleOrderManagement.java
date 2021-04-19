package com.igorzaitcev.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igorzaitcev.inventory.dto.SaleDTO;
import com.igorzaitcev.inventory.exceptions.InvalidArgumentException;
import com.igorzaitcev.inventory.model.Delivery;
import com.igorzaitcev.inventory.model.SaleItem;
import com.igorzaitcev.inventory.model.SaleOrder;
import com.igorzaitcev.inventory.repository.DeliveryRepository;
import com.igorzaitcev.inventory.repository.SaleItemRepository;
import com.igorzaitcev.inventory.repository.SaleOrderRepository;
import com.igorzaitcev.inventory.service.mapper.SaleDTOMapper;

@Service
@Transactional
public class SaleOrderManagement {
	private static final Logger log = LoggerFactory.getLogger(SaleOrderManagement.class);

	private SaleOrderRepository repository;
	private DeliveryRepository deliveryRepository;
	private SaleItemRepository itemRepository;
	private SaleDTOMapper mapper;

	public SaleOrderManagement(SaleOrderRepository repository, DeliveryRepository deliveryRepository,
			SaleItemRepository itemRepository, SaleDTOMapper mapper) {
		super();
		this.repository = repository;
		this.deliveryRepository = deliveryRepository;
		this.itemRepository = itemRepository;
		this.mapper = mapper;
	}

	public void addSaleOrder(SaleDTO saleDTO) {
		SaleOrder order = repository.save(mapper.convertToSaleOrder(saleDTO));
		log.debug("Service Sale Order added: " + order.getId());
		saveDelivery(saleDTO, order);
		log.debug("Service Delivery added: " + order.getId());
		List<SaleItem> items = saleDTO.getItems().stream().map(item -> mapper.convertToSaleItem(item, order))
				.peek(itemRepository::save)
				.collect(Collectors.toList());
		addToStock(items);
	}

	public List<SaleDTO> findSaleOrders() {
		return repository.findAll().stream().map(mapper::convertToSaleDTO).collect(Collectors.toList());
	}

	public SaleDTO findSaleOrder(Long id) {
		Optional<SaleOrder> order = repository.findById(id);
		if (order.isEmpty()) {
			log.debug("Service Sale Order not found: " + id);
			throw new InvalidArgumentException(String.format("No resource found for id (%s)", id));
		}
		return mapper.convertToSaleDTO(order.get());
	}

	public void deleteSaleOrder(Long id) {
		SaleOrder order = repository.findById(id).get();
		if (findDelivery(id).getValidated()) {
			log.debug("Service Sale Order cannot be deleted: " + id);
			throw new InvalidArgumentException(String.format("Order cannot be deleted id (%s)", id));
		}
		removeFromStock(order);
		findSaleItems(id).stream().forEach(item -> itemRepository.deleteById(item.getId()));
		log.debug("Service Delivery deleted: " + id);
		deliveryRepository.deleteById(findDelivery(id).getId());
		log.debug("Service Sale Order deleted: " + id);
		repository.deleteById(id);
	}

	private void removeFromStock(SaleOrder order) {
		findSaleItems(order.getId()).stream().forEach(item -> item.getProduct().getStock()
				.setOnSale(item.getProduct().getStock().getOnSale() - item.getQuantity()));

	}

	private void addToStock(List<SaleItem> items) {
		items.stream().forEach(item -> item.getProduct().getStock()
				.setOnSale(item.getProduct().getStock().getOnSale() + item.getQuantity()));
	}

	private void saveDelivery(SaleDTO saleDTO, SaleOrder order) {
		if (saleDTO.getDeliveryId() != null) {
			deliveryRepository.findById(saleDTO.getDeliveryId()).ifPresent(item -> item.setSaleOrder(order));
		} else {
			deliveryRepository.save(new Delivery(order, false));
		}
	}

	private List<SaleItem> findSaleItems(Long orderId) {
		return itemRepository.findByOrderId(orderId);
	}

	private Delivery findDelivery(Long id) {
		return deliveryRepository.findByOrderId(id).get();
	}
}
