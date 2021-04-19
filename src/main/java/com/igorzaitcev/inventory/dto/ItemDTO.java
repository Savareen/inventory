package com.igorzaitcev.inventory.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public class ItemDTO {

	@Schema(description = "Unique identifier of the Item.", example = "1", required = false)
	private Long id;
	@Schema(description = "Product name.", example = "Boston Beer Company", required = true)
	@NotBlank
	private String productName;
	@Schema(description = "Quantity of the product.", example = "20", required = true)
	@NotNull
	@Positive
	private Integer quantity;

	public ItemDTO() {
	}

	public ItemDTO(String productName, Integer quantity) {
		this.productName = productName;
		this.quantity = quantity;
	}

	public ItemDTO(Long id, String productName, Integer quantity) {
		super();
		this.id = id;
		this.productName = productName;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ItemDTO [id=" + id + ", productName=" + productName + ", quantity=" + quantity + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
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
		ItemDTO other = (ItemDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		return true;
	}

}
