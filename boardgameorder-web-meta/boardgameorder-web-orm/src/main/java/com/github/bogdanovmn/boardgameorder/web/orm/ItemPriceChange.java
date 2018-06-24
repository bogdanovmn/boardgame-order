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
	private Double priceChange;
	private Double priceCurrent;
	private Integer countChange;
	private Integer countCurrent;
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
	public Boolean isPriceChange() {
		return priceChange != null;
	}

	public int getPriceChangeRounded() {
		return (int)Math.round(priceChange);
	}

	public ItemPriceChange setPriceChange(final Double priceChange) {
		this.priceChange = priceChange;
		return this;
	}

	public Integer getCountChange() {
		return countChange;
	}
	public Boolean isCountChange() {
		return countChange != null;
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

	public Double getPriceCurrent() {
		return priceCurrent;
	}

	public int getPriceCurrentRounded() {
		return (int)Math.round(priceCurrent);
	}

	public ItemPriceChange setPriceCurrent(final Double priceCurrent) {
		this.priceCurrent = priceCurrent;
		return this;
	}

	public Integer getCountCurrent() {
		return countCurrent;
	}

	public ItemPriceChange setCountCurrent(final Integer countCurrent) {
		this.countCurrent = countCurrent;
		return this;
	}

	public ItemPriceChange copy() {
		return new ItemPriceChange()
			.setSource(source)
			.setPreviousSource(previousSource)
			.setType(type);
	}

	public String getPriceChangeSign() {
		return (priceChange != null && priceChange > 0) ? "+" : null;
	}

	public String getCountChangeSign() {
		return (countChange != null && countChange > 0) ? "+" : null;
	}
}
