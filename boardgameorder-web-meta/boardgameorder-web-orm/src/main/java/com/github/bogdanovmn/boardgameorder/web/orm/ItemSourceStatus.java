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
public class ItemSourceStatus extends BaseEntity {
	@ManyToOne
	private Source source;
	@ManyToOne
	private Item item;
	@Enumerated(EnumType.STRING)
	private ItemStatus status;
}
