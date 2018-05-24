package com.github.bogdanovmn.boardgameorder.web.orm;

import javax.persistence.*;

@Entity
@Table(
	indexes = {
		@Index(
			columnList = "barcode"
		)
	}
)
class Item extends BaseEntity {
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private Double price;
	@Column(nullable = false)
	private Integer count;
	@Column(length = 20)
	private String barcode;
	private String url;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;

//	@ManyToMany(mappedBy = "items")
//	private Set<User> users;
}
