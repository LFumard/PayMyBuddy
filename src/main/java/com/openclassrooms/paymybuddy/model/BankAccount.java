package com.openclassrooms.paymybuddy.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	@NotEmpty(message = "Veuillez saisir votre IBAN")
	private String iban;
	private String description;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public String getIban() {
		return iban;
	}

	public String getDescription() {
		return description;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BankAccount that)) return false;
		return id == that.id && Objects.equals(iban, that.iban) && Objects.equals(description, that.description) && Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, iban, description, user);
	}
}
