package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntityWithUniqueName extends BaseEntity {
	@Column(unique = true, nullable = false)
	private String name;

	public BaseEntityWithUniqueName() {
		super();
	}

	public BaseEntityWithUniqueName(String name) {
		super();
		this.name = name;
	}

	public BaseEntityWithUniqueName(Integer id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public BaseEntityWithUniqueName setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public boolean equals(Object thatObj) {
		if (this == thatObj) {
			return true;
		}
		if (thatObj == null || getClass() != thatObj.getClass()) {
			return false;
		}
		if (this.getName() == null) {
			return false;
		}

		BaseEntityWithUniqueName that = (BaseEntityWithUniqueName) thatObj;

		return this.getName().equals(that.getName());
	}

	@Override
	public int hashCode() {
		return this.getName() == null
			? 0
			: this.getName().hashCode();
	}
}
