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
import javax.validation.constraints.NotNull;

@Entity(name = "Stock")
@Table(name = "stocks")
public class Stock {

	@Id
	@SequenceGenerator(name = "stock_sequence", sequenceName = "stock_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_sequence")
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@NotNull
	@Column(name = "on_hand", nullable = false)
	private Integer onHand;

	@NotNull
	@Column(name = "forecasted", nullable = false)
	private Integer forecasted;

	@NotNull
	@Column(name = "on_sale", nullable = false)
	private Integer onSale;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	public Stock() {
	}

	public Stock(Integer onHand, Integer forecasted, Integer onSale, Product product) {
		this.onHand = onHand;
		this.forecasted = forecasted;
		this.onSale = onSale;
		this.product = product;
	}

	public Stock(Long id, Integer onHand, Integer forecasted, Integer onSale, Product product) {
		this.id = id;
		this.onHand = onHand;
		this.forecasted = forecasted;
		this.onSale = onSale;
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "Stock [id=" + id + ", onHand=" + onHand + ", forecasted=" + forecasted + ", onSale=" + onSale
				+ ", product=" + product + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((forecasted == null) ? 0 : forecasted.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((onHand == null) ? 0 : onHand.hashCode());
		result = prime * result + ((onSale == null) ? 0 : onSale.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		Stock other = (Stock) obj;
		if (forecasted == null) {
			if (other.forecasted != null)
				return false;
		} else if (!forecasted.equals(other.forecasted))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

}
