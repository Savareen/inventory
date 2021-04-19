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

@Entity(name = "PurchaseItem")
@Table(name = "purchase_items")
public class PurchaseItem {

	@Id
	@SequenceGenerator(name = "purchase_item_sequence", sequenceName = "purchase_item_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_item_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@NotNull
	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
	private PurchaseOrder purchaseOrder;

	public PurchaseItem() {
		super();
	}

	public PurchaseItem(Integer quantity, Product product, PurchaseOrder purchaseOrder) {
		this.quantity = quantity;
		this.product = product;
		this.purchaseOrder = purchaseOrder;
	}

	public PurchaseItem(Long id, Integer quantity, Product product) {
		this.id = id;
		this.quantity = quantity;
		this.product = product;
	}

	public PurchaseItem(Long id, Integer quantity, Product product, PurchaseOrder purchaseOrder) {
		this.id = id;
		this.quantity = quantity;
		this.product = product;
		this.purchaseOrder = purchaseOrder;
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

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@Override
	public String toString() {
		return "PurchaseItem [id=" + id + ", quantity=" + quantity + ", product=" + product + ", purchaseOrder="
				+ purchaseOrder + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((purchaseOrder == null) ? 0 : purchaseOrder.hashCode());
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
		PurchaseItem other = (PurchaseItem) obj;
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
		if (purchaseOrder == null) {
			if (other.purchaseOrder != null)
				return false;
		} else if (!purchaseOrder.equals(other.purchaseOrder))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		return true;
	}

}
