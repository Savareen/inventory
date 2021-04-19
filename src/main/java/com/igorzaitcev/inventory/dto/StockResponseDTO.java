package com.igorzaitcev.inventory.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public class StockResponseDTO {

	@Schema(description = "Unique identifier of the Stock.", example = "1", required = true)
	@NotNull
	@Positive
	private Long stockId;
	@Schema(description = "Quantity of the product on hand.", example = "30", required = true)
	@NotNull
	@Positive
	private Integer onHand;

	public StockResponseDTO() {
	}

	public StockResponseDTO(Long stockId, Integer onHand) {
		super();
		this.stockId = stockId;
		this.onHand = onHand;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Integer getOnHand() {
		return onHand;
	}

	public void setOnHand(Integer onHand) {
		this.onHand = onHand;
	}

	@Override
	public String toString() {
		return "StockResponseDTO [stockId=" + stockId + ", onHand=" + onHand + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((onHand == null) ? 0 : onHand.hashCode());
		result = prime * result + ((stockId == null) ? 0 : stockId.hashCode());
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
		StockResponseDTO other = (StockResponseDTO) obj;
		if (onHand == null) {
			if (other.onHand != null)
				return false;
		} else if (!onHand.equals(other.onHand))
			return false;
		if (stockId == null) {
			if (other.stockId != null)
				return false;
		} else if (!stockId.equals(other.stockId))
			return false;
		return true;
	}

}
