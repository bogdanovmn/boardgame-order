package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"user_id", "item_id"}
		)
	}
)
public class UserOrderItem extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(nullable = false)
	private Integer count = 1;

	@Column(nullable = false)
	private Date updated = new Date();

	public UserOrderItem() {
	}

	public User getUser() {
		return user;
	}

	public UserOrderItem setUser(User user) {
		this.user = user;
		return this;
	}

	public Date getUpdated() {
		return updated;
	}

	public UserOrderItem setUpdated(Date updated) {
		this.updated = updated;
		return this;
	}

	public Item getItem() {
		return item;
	}

	public UserOrderItem setItem(final Item item) {
		this.item = item;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public UserOrderItem setCount(final Integer count) {
		this.count = count;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserOrderItem that = (UserOrderItem) o;
		return Objects.equals(user, that.user) &&
			Objects.equals(item, that.item) &&
			Objects.equals(count, that.count) &&
			Objects.equals(updated, that.updated);
	}

	@Override
	public int hashCode() {

		return Objects.hash(user, item, count, updated);
	}

	public UserOrderItem incCount(final int increment) {
		count += increment;
		return this;
	}
}
