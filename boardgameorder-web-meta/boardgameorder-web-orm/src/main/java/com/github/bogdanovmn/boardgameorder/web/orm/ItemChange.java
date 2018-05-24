package com.github.bogdanovmn.boardgameorder.web.orm;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"item_id", "source_id"}
		)
	}
)
public class ItemChange extends BaseEntity {
	@ManyToOne
	private Source source;
	@ManyToOne
	private Item item;
	private Double price;
	private Integer count;
}
