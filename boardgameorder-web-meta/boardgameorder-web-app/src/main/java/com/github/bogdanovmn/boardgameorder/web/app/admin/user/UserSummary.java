package com.github.bogdanovmn.boardgameorder.web.app.admin.user;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.BaseEntityWithUniqueName;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;

import java.util.Date;
import java.util.stream.Collectors;

class UserSummary {
	private final Integer id;
	private final String name;
	private final String email;
	private final Date registerDate;
	private final String roles;

	UserSummary(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.registerDate = user.getRegisterDate();
		this.roles = user.getRoles().stream()
			.map(BaseEntityWithUniqueName::getName)
			.collect(Collectors.joining(", "));
	}
}
