package com.igorzaitcev.inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity(name = "Product")
@Table(name = "products")
public class Product {

	@Id
	@SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@NotBlank
	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "units", nullable = false)
	private UnitOfMeasure units;

	@NotNull
	@Column(name = "cost", nullable = false)
	private Integer cost;

	@NotNull
	@Positive
	@Column(name = "price", nullable = false)
	private Integer price;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "product")
	private Stock stock;

	public Product() {
	}

	public Product(String name, UnitOfMeasure units, Integer cost, Integer price) {
		this.name = name;
		this.units = units;
		this.cost = cost;
		this.price = price;
	}

	public Product(Long id, String name, UnitOfMeasure units, Integer cost, Integer price) {
		this.id = id;
		this.name = name;
		this.units = units;
		this.cost = cost;
		this.price = price;
	}

	public Product(Long id, String name, UnitOfMeasure units, Integer cost, Integer price, Stock stock) {
		this.id = id;
		this.name = name;
		this.units = units;
		this.cost = cost;
		this.price = price;
		this.stock = stock;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", units=" + units + ", cost=" + cost + ", price=" + price
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
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
		Product other = (Product) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (units != other.units)
			return false;
		return true;
	}

}
