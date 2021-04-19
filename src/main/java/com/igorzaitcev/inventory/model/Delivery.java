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

@Entity(name = "Delivery")
@Table(name = "deliveries")
public class Delivery {

	@Id
	@SequenceGenerator(name = "delivery_sequence", sequenceName = "delivery_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "sale_order_id", referencedColumnName = "id")
	private SaleOrder saleOrder;

	@Column(columnDefinition = "boolean default false", nullable = false)
	private Boolean validated;

	public Delivery() {
	}

	public Delivery(Long id, Boolean validated) {
		this.id = id;
		this.validated = validated;
	}

	public Delivery(SaleOrder saleOrder, Boolean validated) {
		this.saleOrder = saleOrder;
		this.validated = validated;
	}

	public Delivery(Long id, SaleOrder saleOrder, Boolean validated) {
		this.id = id;
		this.saleOrder = saleOrder;
		this.validated = validated;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	@Override
	public String toString() {
		return "Delivery [id=" + id + ", saleOrder=" + saleOrder + ", validated=" + validated + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((saleOrder == null) ? 0 : saleOrder.hashCode());
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
		Delivery other = (Delivery) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (saleOrder == null) {
			if (other.saleOrder != null)
				return false;
		} else if (!saleOrder.equals(other.saleOrder))
			return false;
		if (validated == null) {
			if (other.validated != null)
				return false;
		} else if (!validated.equals(other.validated))
			return false;
		return true;
	}

}
