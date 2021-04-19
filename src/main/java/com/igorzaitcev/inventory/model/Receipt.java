package com.igorzaitcev.inventory.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "Receipt")
@Table(name = "receipts")
public class Receipt {

	@Id
	@SequenceGenerator(name = "receipt_sequence", sequenceName = "receipt_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receipt_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
	private PurchaseOrder purchaseOrder;

	@Column(columnDefinition = "boolean default false", nullable = false)
	private Boolean validated;

	public Receipt() {
	}

	public Receipt(PurchaseOrder purchaseOrder, Boolean validated) {
		super();
		this.purchaseOrder = purchaseOrder;
		this.validated = validated;
	}

	public Receipt(Long id, Boolean validated) {
		this.id = id;
		this.validated = validated;
	}

	public Receipt(Long id, PurchaseOrder purchaseOrder, Boolean validated) {
		this.id = id;
		this.purchaseOrder = purchaseOrder;
		this.validated = validated;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	@Override
	public String toString() {
		return "Receipt [id=" + id + ", purchaseOrder=" + purchaseOrder + ", validated=" + validated + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((purchaseOrder == null) ? 0 : purchaseOrder.hashCode());
		result = prime * result + ((validated == null) ? 0 : validated.hashCode());
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
		Receipt other = (Receipt) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (purchaseOrder == null) {
			if (other.purchaseOrder != null)
				return false;
		} else if (!purchaseOrder.equals(other.purchaseOrder))
			return false;
		if (validated == null) {
			if (other.validated != null)
				return false;
		} else if (!validated.equals(other.validated))
			return false;
		return true;
	}

}
