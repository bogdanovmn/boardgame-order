package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class User extends BaseEntityWithUniqueName {
	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String passwordHash;

	@Column(nullable = false)
	private Date registerDate;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
		name = "role2user",
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private Set<UserRole> roles;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
		name = "favorite_items",
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id")
	)
	private Set<Item> favoriteItems;

	public User() {}

	public User(String name) {
		super(name);
	}

	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public User setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		return this;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public User setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
		return this;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public User setRoles(Set<UserRole> roles) {
		this.roles = roles;
		return this;
	}

	public boolean hasRole(UserRole.Type roleType) {
		return roles.contains(new UserRole(roleType.name()));
	}
}
