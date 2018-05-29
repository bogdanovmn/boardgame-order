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
	@Column(length = 20)
	private String barcode;
	private String url;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;

	public String getTitle() {
		return title;
	}

	public Item setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getBarcode() {
		return barcode;
	}

	public Item setBarcode(String barcode) {
		this.barcode = barcode;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Item setUrl(String url) {
		this.url = url;
		return this;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public Item setPublisher(Publisher publisher) {
		this.publisher = publisher;
		return this;
	}
}
