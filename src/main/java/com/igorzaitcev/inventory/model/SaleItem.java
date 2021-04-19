package com.igorzaitcev.inventory.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "SaleItem")
@Table(name = "sale_items")
public class SaleItem {

	@Id
	@SequenceGenerator(name = "sale_item_sequence", sequenceName = "sale_item_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_item_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@NotNull
	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "sale_order_id", referencedColumnName = "id")
	private SaleOrder saleOrder;

	public SaleItem() {
	}

	public SaleItem(Integer quantity, Product product) {
		this.quantity = quantity;
		this.product = product;
	}

	public SaleItem(Long id, Integer quantity, Product product) {
		this.id = id;
		this.quantity = quantity;
		this.product = product;
	}

	public SaleItem(Long id, Integer quantity, Product product, SaleOrder saleOrder) {
		this.id = id;
		this.quantity = quantity;
		this.product = product;
		this.saleOrder = saleOrder;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	@Override
	public String toString() {
		return "SaleItem [id=" + id + ", quantity=" + quantity + ", product=" + product + ", saleOrder=" + saleOrder
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((saleOrder == null) ? 0 : saleOrder.hashCode());
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
		SaleItem other = (SaleItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (saleOrder == null) {
			if (other.saleOrder != null)
				return false;
		} else if (!saleOrder.equals(other.saleOrder))
			return false;
		return true;
	}

}
