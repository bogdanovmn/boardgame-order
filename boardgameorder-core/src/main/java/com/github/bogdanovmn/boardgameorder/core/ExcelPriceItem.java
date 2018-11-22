package com.github.bogdanovmn.boardgameorder.core;

import java.util.regex.Pattern;

public class ExcelPriceItem {
	private static final Pattern VALID_BARCODE_PATTERN = Pattern.compile("^\\d+$");

	private final String group;
	private final String title;
	private final Double price;
	private final Integer count;
	private final String barcode;
	private final String url;

	public ExcelPriceItem(
		String group,
		String title,
		Double price,
		Double count,
		String barcode,
		String url
	  ) {

		this.group = group.replaceAll("\\s+", " ");
		this.title = title.replaceAll("\\s+", " ");
		this.price = price;
		this.count = count == null ? null : count.intValue();
		this.barcode = barcode != null && VALID_BARCODE_PATTERN.matcher(barcode).find() ? barcode : null;
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
		return String.format(
			"ExcelPriceItem{group='%s', title='%s', barcode='%s'}",
				group, title, barcode
		);
	}
}
