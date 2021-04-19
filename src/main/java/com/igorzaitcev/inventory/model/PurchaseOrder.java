package com.igorzaitcev.inventory.model;

import java.time.LocalDate;

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

@Entity(name = "PurchaseOrder")
@Table(name = "purchases")
public class PurchaseOrder {

	@Id
	@SequenceGenerator(name = "purchase_sequence", sequenceName = "purchase_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_sequence")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@NotNull
	@Column(name = "number", nullable = false)
	private Integer number;

	@Column(name = "commitment_date", columnDefinition = "DATE", nullable = false)
	private LocalDate commitmentDate;

	@Column(name = "scheduled_date", columnDefinition = "DATE", nullable = false)
	private LocalDate scheduledDate;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "vendor_id", referencedColumnName = "id")
	private Vendor vendor;

	public PurchaseOrder() {
	}

	public PurchaseOrder(Integer number, LocalDate commitmentDate, LocalDate scheduledDate, Vendor vendor) {
		super();
		this.number = number;
		this.commitmentDate = commitmentDate;
		this.scheduledDate = scheduledDate;
		this.vendor = vendor;
	}

	public PurchaseOrder(Long id, Integer number, LocalDate commitmentDate, LocalDate scheduledDate, Vendor vendor) {
		this.id = id;
		this.number = number;
		this.commitmentDate = commitmentDate;
		this.scheduledDate = scheduledDate;
		this.vendor = vendor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public LocalDate getCommitmentDate() {
		return commitmentDate;
	}

	public void setCommitmentDate(LocalDate commitmentDate) {
		this.commitmentDate = commitmentDate;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	@Override
	public String toString() {
		return "PurchaseOrder [id=" + id + ", number=" + number + ", commitmentDate=" + commitmentDate
				+ ", scheduledDate=" + scheduledDate + ", vendor=" + vendor + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commitmentDate == null) ? 0 : commitmentDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((scheduledDate == null) ? 0 : scheduledDate.hashCode());
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
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
		PurchaseOrder other = (PurchaseOrder) obj;
		if (commitmentDate == null) {
			if (other.commitmentDate != null)
				return false;
		} else if (!commitmentDate.equals(other.commitmentDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (scheduledDate == null) {
			if (other.scheduledDate != null)
				return false;
		} else if (!scheduledDate.equals(other.scheduledDate))
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (!vendor.equals(other.vendor))
			return false;
		return true;
	}

}
