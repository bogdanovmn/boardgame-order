package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"item_id", "source_id"}
		)
	}
)
public class ItemPrice extends BaseEntity {
	@ManyToOne
	private Source source;
	@ManyToOne
	private Item item;
	@Column(nullable = false)
	private Double price;
	@Column(nullable = false)
	private Integer count;

	public int getRoundedPrice() {
		return (int)Math.round(price);
	}
	public Source getSource() {
		return source;
	}

	public ItemPrice setSource(Source source) {
		this.source = source;
		return this;
	}

	public Item getItem() {
		return item;
	}

	public ItemPrice setItem(Item item) {
		this.item = item;
		return this;
	}

	public Double getPrice() {
		return price;
	}

	public ItemPrice setPrice(Double price) {
		this.price = price;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public ItemPrice setCount(Integer count) {
		this.count = count;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ItemPrice itemPrice = (ItemPrice) o;
		return Objects.equals(source, itemPrice.source) &&
			Objects.equals(item, itemPrice.item);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, item);
	}
}
