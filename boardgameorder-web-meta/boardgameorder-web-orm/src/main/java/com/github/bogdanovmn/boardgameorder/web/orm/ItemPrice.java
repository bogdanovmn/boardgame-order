package com.github.bogdanovmn.boardgameorder.web.orm;

import javax.persistence.*;

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
}
