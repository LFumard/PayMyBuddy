package com.openclassrooms.paymybuddy.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="user")
public class User implements UserDetails {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
    @Size(max=45)
    @Column(length = 45,nullable = false)
    private String lastName;
	
	@NotNull
    @Size(max=45)
    @Column(length = 45,nullable = false)
    private String firstName;
	
	@NotEmpty(message = "Veuillez saisir votre E-Mail")
	@Email
	@Size(max = 255)
	@Column(length = 255,nullable = false,unique = true)
	private String email;
	
	@NotNull
    @Size(max=255)
    @Column(length = 255,nullable = false)
    private String password;
	
	@Column(precision = 12,scale=2)
    private double solde;
	
	@ManyToMany
	@JoinTable(name = "connected_user",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "friend_user_id"))
	private List<User> friends = new ArrayList<>();

	@OneToMany(mappedBy = "userSender")
	private List<Transaction> transactionsEmit;

	@OneToMany(mappedBy = "userReceiver")
	private List<Transaction> transactionsReceiver;

/*	public User(int id, String lastName, String firstName, String email, String password, double solde, List<User> friends, List<Transaction> transactionsEmit, List<Transaction> transactionsReceiver) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.password = password;
		this.solde = solde;
		this.friends = friends;
		this.transactionsEmit = transactionsEmit;
		this.transactionsReceiver = transactionsReceiver;
	}*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getSolde() {
		return solde;
	}

	public void setSolde(double solde) {
		this.solde = solde;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> listFriend) {
		friends = listFriend;
	}

	public List<Transaction> getTransactionsEmit() {
		return transactionsEmit;
	}

	public void setTransactionsEmit(List<Transaction> transactionsEmit) {
		this.transactionsEmit = transactionsEmit;
	}

	public List<Transaction> getTransactionsReceiver() {
		return transactionsReceiver;
	}

	public void setTransactionsReceiver(List<Transaction> transactionsReceiver) {
		this.transactionsReceiver = transactionsReceiver;
	}

	@Override
	public String toString() {
		return "{" +
//				"id=" + id +
				", lastName='" + lastName + '\'' +
				", firstName='" + firstName + '\'' +
				", email='" + email + '\'' +
//				", password='" + password + '\'' +
				", solde=" + solde +
				/*", friends=" + friends +
				", transactionsEmit=" + transactionsEmit +
				", transactionsReceiver=" + transactionsReceiver +*/
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if(this.getClass().equals(obj.getClass())) {
			return this.toString().equals(obj.toString());
		}
		return false;
	}
}
