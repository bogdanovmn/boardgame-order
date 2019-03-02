package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter

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
