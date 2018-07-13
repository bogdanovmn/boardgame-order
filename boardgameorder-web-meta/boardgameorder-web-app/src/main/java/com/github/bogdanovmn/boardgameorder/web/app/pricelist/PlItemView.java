package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class PlItemView {
	private final ItemPrice itemPrice;
	private final boolean ordered;

	PlItemView(final ItemPrice itemPrice, final boolean ordered) {
		this.itemPrice = itemPrice;
		this.ordered = ordered;
	}

	ItemPrice getPrice() {
		return itemPrice;
	}

	boolean isOrdered() {
		return ordered;
	}

	String getGoogleSearchUrl() throws UnsupportedEncodingException {
		return String.format(
			"https://google.ru/search?q=настольная игра %s купить",
				URLEncoder.encode(
					itemPrice.getItem().getEffectiveTitle(),
					"UTF-8"
				)
		);
	}
}
