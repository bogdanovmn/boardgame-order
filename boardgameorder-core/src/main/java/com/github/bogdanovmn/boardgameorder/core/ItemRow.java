package com.github.bogdanovmn.boardgameorder.core;

class ItemRow {
	private final String group;
	private final String title;
	private final Double price;
	private final Integer count;
	private final String barcode;
	private final String url;

	public ItemRow(
		String group,
		String title,
		Double price,
		Double count,
		String barcode,
		String url
	  ) {

		this.group = group;
		this.title = title;
		this.price = price;
		this.count = count == null ? null : count.intValue();
		this.barcode = barcode;
		this.url = url;
	}

	String getGroup() {
		return group;
	}

	String getTitle() {
		return title;
	}

	Double getPrice() {
		return price;
	}

	Integer getCount() {
		return count;
	}

	String getBarcode() {
		return barcode;
	}

	String getUrl() {
		return url;
	}
}
