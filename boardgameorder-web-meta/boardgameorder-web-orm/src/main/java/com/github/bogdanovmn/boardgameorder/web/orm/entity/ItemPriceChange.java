package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter

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

	public Boolean isPriceChange() {
		return priceChange != null;
	}

	public int getPriceChangeRounded() {
		return (int)Math.round(priceChange);
	}

	public Boolean isCountChange() {
		return countChange != null;
	}

	public int getPriceCurrentRounded() {
		return (int)Math.round(priceCurrent);
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
