package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;

class ItemPriceView {
	private final ItemPrice itemPrice;
	private final boolean ordered;

	ItemPriceView(final ItemPrice itemPrice, final boolean ordered) {
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
