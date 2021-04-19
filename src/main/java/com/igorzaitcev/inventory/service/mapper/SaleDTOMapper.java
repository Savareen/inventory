package com.igorzaitcev.inventory.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.igorzaitcev.inventory.dto.ItemDTO;
import com.igorzaitcev.inventory.dto.SaleDTO;
import com.igorzaitcev.inventory.model.Delivery;
import com.igorzaitcev.inventory.model.SaleItem;
import com.igorzaitcev.inventory.model.SaleOrder;
import com.igorzaitcev.inventory.repository.CustomerRepository;
import com.igorzaitcev.inventory.repository.DeliveryRepository;
import com.igorzaitcev.inventory.repository.ProductRepository;
import com.igorzaitcev.inventory.repository.SaleItemRepository;

@Component
public class SaleDTOMapper {

	private ProductRepository productRepository;
	private CustomerRepository customerRepository;
	private DeliveryRepository deliveryRepository;
	private SaleItemRepository itemRepository;

	public SaleDTOMapper(ProductRepository productRepository, CustomerRepository customerRepository,
			DeliveryRepository deliveryRepository, SaleItemRepository itemRepository) {
		this.productRepository = productRepository;
		this.customerRepository = customerRepository;
		this.deliveryRepository = deliveryRepository;
		this.itemRepository = itemRepository;
	}

	public SaleOrder convertToSaleOrder(SaleDTO saleDTO) {
		SaleOrder order = new SaleOrder();
		order.setId(saleDTO.getOrderId());
		order.setCustomer(customerRepository.findByName(saleDTO.getCustomerName()).get());
		order.setNumber(saleDTO.getOrderNumber());
		order.setCommitmentDate(saleDTO.getCommitmentDate());
		order.setScheduledDate(saleDTO.getScheduledDate());
		return order;
	}

	public SaleDTO convertToSaleDTO(SaleOrder order) {
		SaleDTO saleDTO = new SaleDTO();
		Long orderId = order.getId();
		saleDTO.setOrderId(orderId);
		saleDTO.setOrderNumber(order.getNumber());
		saleDTO.setCommitmentDate(order.getCommitmentDate());
		saleDTO.setScheduledDate(order.getScheduledDate());
		saleDTO.setCustomerName(order.getCustomer().getName());
		saleDTO.setDeliveryId(findDelivery(orderId).getId());
		List<ItemDTO> items = findSaleItems(orderId).stream().map(this::convertToItemDTO).collect(Collectors.toList());
		saleDTO.setItems(items);
		return saleDTO;

	}

	public ItemDTO convertToItemDTO(SaleItem saleItem) {
		ItemDTO item = new ItemDTO();
		item.setId(saleItem.getId());
		item.setProductName(saleItem.getProduct().getName());
		item.setQuantity(saleItem.getQuantity());
		return item;
	}

	public SaleItem convertToSaleItem(ItemDTO itemDTO, SaleOrder order) {
		SaleItem item = new SaleItem();
		item.setId(itemDTO.getId());
		item.setProduct(productRepository.findByName(itemDTO.getProductName()).get());
		item.setQuantity(itemDTO.getQuantity());
		item.setSaleOrder(order);
		return item;
	}

	private List<SaleItem> findSaleItems(Long orderId) {
		return itemRepository.findByOrderId(orderId);
	}

	private Delivery findDelivery(Long id) {
		return deliveryRepository.findByOrderId(id).get();
	}
}
