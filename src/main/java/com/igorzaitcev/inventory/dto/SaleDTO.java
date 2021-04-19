package com.igorzaitcev.inventory.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public class SaleDTO {

	@Schema(description = "Unique identifier of the Sale order.", example = "1", required = false)
	private Long orderId;
	@Schema(description = "Sale order number.", example = "127456", required = true)
	@NotNull
	@Positive
	private Integer orderNumber;
	@Schema(description = "Commitment date of sale order.", example = "2021-05-05", required = true)
	@NotNull
	private LocalDate commitmentDate;
	@Schema(description = "Scheduled date of sale order.", example = "2021-05-05", required = true)
	@NotNull
	private LocalDate scheduledDate;
	@Schema(description = "Customer name.", example = "Boston Beer Company", required = true)
	@NotBlank
	private String customerName;
	@Schema(description = "Delivery id.", example = "1", required = false)
	private Long deliveryId;
	@Schema(description = "Sale items.", example = "1, Boston Beer Company, 20", required = true)
	@NotEmpty
	private List<ItemDTO> items;

	public SaleDTO() {
	}

	public SaleDTO(Integer orderNumber, LocalDate commitmentDate, LocalDate scheduledDate, String customerName,
			Long deliveryId,
			List<ItemDTO> items) {
		super();
		this.orderNumber = orderNumber;
		this.commitmentDate = commitmentDate;
		this.scheduledDate = scheduledDate;
		this.customerName = customerName;
		this.deliveryId = deliveryId;
		this.items = items;
	}

	public SaleDTO(Long orderId, Integer orderNumber, LocalDate commitmentDate, LocalDate scheduledDate,
			String customerName, Long deliveryId, List<ItemDTO> items) {
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.commitmentDate = commitmentDate;
		this.scheduledDate = scheduledDate;
		this.customerName = customerName;
		this.deliveryId = deliveryId;
		this.items = items;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LocalDate getCommitmentDate() {
		return commitmentDate;
	}

	public void setCommitmentDate(LocalDate commitmentDate) {
		this.commitmentDate = commitmentDate;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "SaleDTO [orderId=" + orderId + ", orderNumber=" + orderNumber + ", commitmentDate=" + commitmentDate
				+ ", scheduledDate=" + scheduledDate + ", customerName=" + customerName + ", deliveryId=" + deliveryId
				+ ", items=" + items + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commitmentDate == null) ? 0 : commitmentDate.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((deliveryId == null) ? 0 : deliveryId.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((orderNumber == null) ? 0 : orderNumber.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaleDTO other = (SaleDTO) obj;
		if (commitmentDate == null) {
			if (other.commitmentDate != null)
				return false;
		} else if (!commitmentDate.equals(other.commitmentDate))
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (deliveryId == null) {
			if (other.deliveryId != null)
				return false;
		} else if (!deliveryId.equals(other.deliveryId))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (orderNumber == null) {
			if (other.orderNumber != null)
				return false;
		} else if (!orderNumber.equals(other.orderNumber))
			return false;
		if (scheduledDate == null) {
			if (other.scheduledDate != null)
				return false;
		} else if (!scheduledDate.equals(other.scheduledDate))
			return false;
		return true;
	}

}
