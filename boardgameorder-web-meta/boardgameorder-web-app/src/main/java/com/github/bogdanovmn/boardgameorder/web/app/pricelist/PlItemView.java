package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;

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
}
