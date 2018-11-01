package com.github.bogdanovmn.boardgameorder.web.app.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

class UserRegistrationForm {
	@NotNull
	@Size(min=3, max=20)
	private String name;

	@NotNull
	@Size(min=1, max=32)
	private String password;

	@NotNull
	@Size(min=1, max=32)
	private String passwordConfirm;

	@NotNull
	private String email;

	@NotNull
	@Size(min = 32, max = 32)
	private String inviteCode;


	String getName() {
		return name;
	}

	public UserRegistrationForm setName(String name) {
		this.name = name;
		return this;
	}

	String getPassword() {
		return password;
	}

	public UserRegistrationForm setPassword(String password) {
		this.password = password;
		return this;
	}

	String getPasswordConfirm() {
		return passwordConfirm;
	}

	public UserRegistrationForm setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
		return this;
	}

	String getEmail() {
		return email;
	}

	public UserRegistrationForm setEmail(String email) {
		this.email = email;
		return this;
	}

	String getInviteCode() {
		return inviteCode;
	}
}
