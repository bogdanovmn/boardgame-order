package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntityWithUniqueName;

import javax.persistence.Entity;

@Entity
public class Publisher extends BaseEntityWithUniqueName {
	public String getNormalizedName() {
		return getName().replaceFirst("^[\\d.\\s]+", "");
	}

	@Override
	public String toString() {
		return String.format("Publisher{id=%s, name=%s}", getId(), getName());
	}
}
