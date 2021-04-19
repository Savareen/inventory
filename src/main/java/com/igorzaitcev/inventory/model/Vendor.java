package com.igorzaitcev.inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity(name = "Vendor")
@Table(name = "vendors")
public class Vendor {

	@Schema(description = "Unique identifier of the Vendor.", example = "1", required = false)
	@Id
	@SequenceGenerator(name = "vendor_sequence", sequenceName = "vendor_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendor_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Schema(description = "Name of the Vendor.", example = "Boston Beer Company", required = true)
	@NotBlank
	@Column(name = "name", nullable = false)
	private String name;

	@Schema(description = "Address of the Vendor.", example = "30 Germania St, Boston, MA 02130, United States", required = true)
	@NotBlank
	@Column(name = "address", nullable = false)
	private String address;

	public Vendor() {
	}

	public Vendor(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public Vendor(Long id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Vendor [id=" + id + ", name=" + name + ", address=" + address + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Vendor other = (Vendor) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
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
		return true;
	}

}
