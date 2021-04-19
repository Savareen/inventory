package com.igorzaitcev.inventory.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.igorzaitcev.inventory.model.UnitOfMeasure;

import io.swagger.v3.oas.annotations.media.Schema;

public class StockRequestDTO {

	@Schema(description = "Unique identifier of the Stock.", example = "1", required = true)
	@NotNull
	private Long stockId;
	@Schema(description = "Product name.", example = "Boston Beer", required = true)
	@NotBlank
	private String productName;
	@Schema(description = "Units of Measure for a product.", example = "LITER", required = true)
	private UnitOfMeasure units;
	@Schema(description = "Quantity of the product on hand.", example = "30", required = true)
	@NotNull
	private Integer onHand;
	@Schema(description = "Quantity of the product forecasted.", example = "30", required = true)
	@NotNull
	private Integer forecasted;
	@Schema(description = "Quantity of the product on sale.", example = "30", required = true)
	@NotNull
	private Integer onSale;

	public StockRequestDTO() {
	}

	public StockRequestDTO(Long stockId, String productName, UnitOfMeasure units, Integer onHand, Integer forecasted,
			Integer onSale) {
		super();
		this.stockId = stockId;
		this.productName = productName;
		this.units = units;
		this.onHand = onHand;
		this.forecasted = forecasted;
		this.onSale = onSale;
	}

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public UnitOfMeasure getUnits() {
		return units;
	}

	public void setUnits(UnitOfMeasure units) {
		this.units = units;
	}

	public Integer getOnHand() {
		return onHand;
	}

	public void setOnHand(Integer onHand) {
		this.onHand = onHand;
	}

	public Integer getForecasted() {
		return forecasted;
	}

	public void setForecasted(Integer forecasted) {
		this.forecasted = forecasted;
	}

	public Integer getOnSale() {
		return onSale;
	}

	public void setOnSale(Integer onSale) {
		this.onSale = onSale;
	}

	@Override
	public String toString() {
		return "StockDTO [stockId=" + stockId + ", productName=" + productName + ", units=" + units + ", onHand="
				+ onHand + ", forecasted=" + forecasted + ", onSale=" + onSale + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((forecasted == null) ? 0 : forecasted.hashCode());
		result = prime * result + ((onHand == null) ? 0 : onHand.hashCode());
		result = prime * result + ((onSale == null) ? 0 : onSale.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((stockId == null) ? 0 : stockId.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
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
		StockRequestDTO other = (StockRequestDTO) obj;
		if (forecasted == null) {
			if (other.forecasted != null)
				return false;
		} else if (!forecasted.equals(other.forecasted))
			return false;
		if (onHand == null) {
			if (other.onHand != null)
				return false;
		} else if (!onHand.equals(other.onHand))
			return false;
		if (onSale == null) {
			if (other.onSale != null)
				return false;
		} else if (!onSale.equals(other.onSale))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (stockId == null) {
			if (other.stockId != null)
				return false;
		} else if (!stockId.equals(other.stockId))
			return false;
		if (units != other.units)
			return false;
		return true;
	}

}
