package com.github.bogdanovmn.boardgameorder.web.orm;

import javax.persistence.*;

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"source_id", "previous_source_id", "item_id"}
		)
	}
)
public class ItemPriceChange extends BaseEntity {
	@ManyToOne
	private Source source;
	@ManyToOne
	private Source previousSource;
	@ManyToOne
	private Item item;
	@Column(nullable = false)
	private Double priceChange;
	@Column(nullable = false)
	private Integer countChange;
	@Enumerated(EnumType.STRING)
	private ItemPriceChangeType type;

	public Source getSource() {
		return source;
	}

	public ItemPriceChange setSource(final Source source) {
		this.source = source;
		return this;
	}

	public Source getPreviousSource() {
		return previousSource;
	}

	public ItemPriceChange setPreviousSource(final Source previousSource) {
		this.previousSource = previousSource;
		return this;
	}

	public Item getItem() {
		return item;
	}

	public ItemPriceChange setItem(final Item item) {
		this.item = item;
		return this;
	}

	public Double getPriceChange() {
		return priceChange;
	}

	public ItemPriceChange setPriceChange(final Double priceChange) {
		this.priceChange = priceChange;
		return this;
	}

	public Integer getCountChange() {
		return countChange;
	}

	public ItemPriceChange setCountChange(final Integer countChange) {
		this.countChange = countChange;
		return this;
	}

	public ItemPriceChangeType getType() {
		return type;
	}

	public ItemPriceChange setType(final ItemPriceChangeType type) {
		this.type = type;
		return this;
	}

	public ItemPriceChange copy() {
		return new ItemPriceChange()
			.setSource(source)
			.setPreviousSource(previousSource)
			.setType(type);
	}
}
