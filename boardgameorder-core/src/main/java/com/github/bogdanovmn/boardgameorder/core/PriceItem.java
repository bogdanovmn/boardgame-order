package com.github.bogdanovmn.boardgameorder.core;

public class PriceItem {
	private final String group;
	private final String title;
	private final Double price;
	private final Integer count;
	private final String barcode;
	private final String url;

	public PriceItem(
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

	public String getGroup() {
		return group;
	}

	public String getTitle() {
		return title;
	}

	public Double getPrice() {
		return price;
	}

	public Integer getCount() {
		return count;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "PriceItem{" +
			"group='" + group + '\'' +
			", title='" + title + '\'' +
			'}';
	}
}
