package com.github.bogdanovmn.boardgameorder.web.orm;

import javax.persistence.Entity;

@Entity
public class Publisher extends BaseEntityWithUniqueName {
	public String getNormalizedName() {
		return getName().replaceFirst("^[\\d.\\s]+", "");
	}

	@Override
	public String toString() {
		return String.format("Publisher{name=%s}", getName());
	}
}
