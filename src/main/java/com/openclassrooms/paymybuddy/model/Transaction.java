package com.openclassrooms.paymybuddy.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Transaction {
	
	@Id
	@Column(name = "id", nullable = false)
	private int id;
	@ManyToOne
	@JoinColumn(name = "user_id_payer")
	private User userSender;
	@ManyToOne
	@JoinColumn(name = "user_id_beneficiary")
	private User userReceiver;
	@Column(columnDefinition = "DATETIME default CURRENT_TIMESTAMP", insertable = false)
	private Date created_dt;
	private String description;
	private Double amount;

	public User getUserSender() {
		return userSender;
	}

	public void setUserSender(User userSender) {
		this.userSender = userSender;
	}

	public User getUserReceiver() {
		return userReceiver;
	}

	public void setUserReceiver(User userReceiver) {
		this.userReceiver = userReceiver;
	}

	public Date getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(Date created_dt) {
		this.created_dt = created_dt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		return this.toString() == o.toString();
	/*	if (!(o instanceof Transaction that)) return false;
		return id == that.id && Objects.equals(userSender, that.userSender) && Objects.equals(userReceiver, that.userReceiver) && Objects.equals(created_dt, that.created_dt) && Objects.equals(description, that.description) && Objects.equals(amount, that.amount);*/
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"id=" + id +
				", userSender=" + userSender +
				", userReceiver=" + userReceiver +
				", created_dt=" + created_dt +
				", description='" + description + '\'' +
				", amount=" + amount +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userSender, userReceiver, created_dt, description, amount);
	}
}
