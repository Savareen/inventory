package com.igorzaitcev.inventory.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public class PurchaseDTO {

	@Schema(description = "Unique identifier of the Purchase order.", example = "1", required = false)
	private Long orderId;
	@Schema(description = "Purchase order number.", example = "127456", required = true)
	@NotNull
	@Positive
	private Integer orderNumber;
	@Schema(description = "Commitment date of purchase order.", example = "2021-05-05", required = true)
	@NotNull
	private LocalDate commitmentDate;
	@Schema(description = "Scheduled date of purchase order.", example = "2021-05-05", required = true)
	@NotNull
	private LocalDate scheduledDate;
	@Schema(description = "Vendor name.", example = "Boston Beer Company", required = true)
	@NotBlank
	private String vendorName;
	@Schema(description = "Receipt id.", example = "1", required = false)
	private Long receiptId;
	@Schema(description = "Purchase items.", example = "1, Boston Beer Company, 20", required = true)
	@NotEmpty
	private List<ItemDTO> items;

	public PurchaseDTO() {
	}

	public PurchaseDTO(Integer orderNumber, LocalDate commitmentDate, LocalDate scheduledDate, String vendorName,
			Long receiptId,
			List<ItemDTO> items) {
		this.orderNumber = orderNumber;
		this.commitmentDate = commitmentDate;
		this.scheduledDate = scheduledDate;
		this.vendorName = vendorName;
		this.receiptId = receiptId;
		this.items = items;
	}

	public PurchaseDTO(Long orderId, Integer orderNumber, LocalDate commitmentDate, LocalDate scheduledDate,
			String vendorName, Long receiptId, List<ItemDTO> items) {
		this.orderId = orderId;
		this.orderNumber = orderNumber;
		this.commitmentDate = commitmentDate;
		this.scheduledDate = scheduledDate;
		this.vendorName = vendorName;
		this.receiptId = receiptId;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Long getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "PurchaseDTO [orderId=" + orderId + ", orderNumber=" + orderNumber + ", commitmentDate=" + commitmentDate
				+ ", scheduledDate=" + scheduledDate + ", vendorName=" + vendorName + ", receiptId=" + receiptId
				+ ", items=" + items + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commitmentDate == null) ? 0 : commitmentDate.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((orderNumber == null) ? 0 : orderNumber.hashCode());
		result = prime * result + ((receiptId == null) ? 0 : receiptId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((vendorName == null) ? 0 : vendorName.hashCode());
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
		PurchaseDTO other = (PurchaseDTO) obj;
		if (commitmentDate == null) {
			if (other.commitmentDate != null)
				return false;
		} else if (!commitmentDate.equals(other.commitmentDate))
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
		if (receiptId == null) {
			if (other.receiptId != null)
				return false;
		} else if (!receiptId.equals(other.receiptId))
			return false;
		if (scheduledDate == null) {
			if (other.scheduledDate != null)
				return false;
		} else if (!scheduledDate.equals(other.scheduledDate))
			return false;
		if (vendorName == null) {
			if (other.vendorName != null)
				return false;
		} else if (!vendorName.equals(other.vendorName))
			return false;
		return true;
	}

}
