package com.igorzaitcev.inventory.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public class DeliveryDTO {

	@Schema(description = "Unique identifier of the Delivery.", example = "1", required = true)
	@NotNull
	@Positive
	private Long deliveryId;
	@Schema(description = "Validation of the Delivery.", example = "false", required = true)
	@NotNull
	private Boolean validated;
	@Schema(description = "Commitment Date of the Order.", example = "2021-05-06", required = true)
	@NotNull
	private LocalDate commitmentDate;
	@Schema(description = "Scheduled Date of the Delivery.", example = "2021-05-06", required = true)
	@NotNull
	private LocalDate scheduledDate;
	@Schema(description = "Customer name.", example = "Boston Beer Company", required = true)
	@NotBlank
	private String customerName;
	@Schema(description = "Sale items.", example = "1, Boston Beer Company, 20", required = true)
	@NotEmpty
	private List<ItemDTO> items;

	public DeliveryDTO() {
	}

	public DeliveryDTO(Long deliveryId, Boolean validated, LocalDate commitmentDate, LocalDate scheduledDate,
			String customerName, List<ItemDTO> items) {
		this.deliveryId = deliveryId;
		this.validated = validated;
		this.commitmentDate = commitmentDate;
		this.scheduledDate = scheduledDate;
		this.customerName = customerName;
		this.items = items;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
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

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "DeliveryDTO [deliveryId=" + deliveryId + ", validated=" + validated + ", commitmentDate="
				+ commitmentDate + ", scheduledDate=" + scheduledDate + ", customerName=" + customerName + ", items="
				+ items + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commitmentDate == null) ? 0 : commitmentDate.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((deliveryId == null) ? 0 : deliveryId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((validated == null) ? 0 : validated.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
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
		DeliveryDTO other = (DeliveryDTO) obj;
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
		if (deliveryId == null) {
			if (other.deliveryId != null)
				return false;
		} else if (!deliveryId.equals(other.deliveryId))
			return false;
		if (scheduledDate == null) {
			if (other.scheduledDate != null)
				return false;
		} else if (!scheduledDate.equals(other.scheduledDate))
			return false;
		if (validated == null) {
			if (other.validated != null)
				return false;
		} else if (!validated.equals(other.validated))
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		return true;
	}

}
