package com.igorzaitcev.inventory.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public class ReceiptDTO {

	@Schema(description = "Unique identifier of the Receipt.", example = "1", required = true)
	@NotNull
	@Positive
	private Long receiptId;
	@Schema(description = "Validation of the Receipt.", example = "false", required = true)
	@NotNull
	private Boolean validated;
	@Schema(description = "Commitment Date of the Order.", example = "2021-05-06", required = true)
	@NotNull
	private LocalDate commitmentDate;
	@Schema(description = "Scheduled Date of the Delivery.", example = "2021-05-06", required = true)
	@NotNull
	private LocalDate scheduledDate;
	@Schema(description = "Vendor name.", example = "Boston Beer Company", required = true)
	@NotBlank
	private String vendorName;
	@Schema(description = "Purchase items.", example = "1, Boston Beer Company, 20", required = true)
	@NotEmpty
	private List<ItemDTO> items;

	public ReceiptDTO() {
	}

	public ReceiptDTO(Long receiptId, Boolean validated, LocalDate commitmentDate, LocalDate scheduledDate,
			String vendorName, List<ItemDTO> items) {
		this.receiptId = receiptId;
		this.validated = validated;
		this.commitmentDate = commitmentDate;
		this.scheduledDate = scheduledDate;
		this.vendorName = vendorName;
		this.items = items;
	}

	public Long getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "ReceiptDTO [receiptId=" + receiptId + ", validated=" + validated + ", commitmentDate=" + commitmentDate
				+ ", scheduledDate=" + scheduledDate + ", vendorName=" + vendorName + ", items=" + items + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commitmentDate == null) ? 0 : commitmentDate.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((receiptId == null) ? 0 : receiptId.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((validated == null) ? 0 : validated.hashCode());
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
		ReceiptDTO other = (ReceiptDTO) obj;
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
		if (validated == null) {
			if (other.validated != null)
				return false;
		} else if (!validated.equals(other.validated))
			return false;
		if (vendorName == null) {
			if (other.vendorName != null)
				return false;
		} else if (!vendorName.equals(other.vendorName))
			return false;
		return true;
	}

}
