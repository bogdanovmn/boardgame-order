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
public class Item extends BaseEntity {
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


	String getTitle() {
		return title;
	}

	Item setTitle(String title) {
		this.title = title;
		return this;
	}

	Double getPrice() {
		return price;
	}

	Item setPrice(Double price) {
		this.price = price;
		return this;
	}

	Integer getCount() {
		return count;
	}

	Item setCount(Integer count) {
		this.count = count;
		return this;
	}

	String getBarcode() {
		return barcode;
	}

	Item setBarcode(String barcode) {
		this.barcode = barcode;
		return this;
	}

	String getUrl() {
		return url;
	}

	Item setUrl(String url) {
		this.url = url;
		return this;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	Item setPublisher(Publisher publisher) {
		this.publisher = publisher;
		return this;
	}
}
