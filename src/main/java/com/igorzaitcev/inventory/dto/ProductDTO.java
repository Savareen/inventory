package com.igorzaitcev.inventory.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.igorzaitcev.inventory.model.UnitOfMeasure;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProductDTO {

	@Schema(description = "Unique identifier of the Product.", example = "1", required = false)
	private Long productId;
	@Schema(description = "Product name.", example = "Boston Beer", required = true)
	@NotBlank
	private String name;
	@Schema(description = "Units of Measure for a product.", example = "LITER", required = true)
	private UnitOfMeasure units;
	@Schema(description = "Product cost.", example = "100", required = true)
	@NotNull
	private Integer cost;
	@Schema(description = "Product price.", example = "100", required = true)
	@NotNull
	private Integer price;

	public ProductDTO() {
	}

	public ProductDTO(Long productId, String name, UnitOfMeasure units, Integer cost, Integer price) {
		super();
		this.productId = productId;
		this.name = name;
		this.units = units;
		this.cost = cost;
		this.price = price;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UnitOfMeasure getUnits() {
		return units;
	}

	public void setUnits(UnitOfMeasure units) {
		this.units = units;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "ProductDTO [productId=" + productId + ", name=" + name + ", units=" + units + ", cost=" + cost
				+ ", price=" + price + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
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
		ProductDTO other = (ProductDTO) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (units != other.units)
			return false;
		return true;
	}

}
