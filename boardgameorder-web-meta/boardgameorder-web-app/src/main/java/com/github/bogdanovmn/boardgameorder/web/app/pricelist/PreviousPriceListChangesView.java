package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPriceChange;
import com.github.bogdanovmn.boardgameorder.web.orm.ItemPriceChangeType;

import java.util.List;
import java.util.stream.Collectors;

class PreviousPriceListChangesView {
	private final List<ItemPriceChange> changes;

	PreviousPriceListChangesView(final List<ItemPriceChange> changes) {
		this.changes = changes;
	}

	List<ItemPriceChange> getNewItems() {
		return changes.stream()
			.filter(x -> x.getType().equals(ItemPriceChangeType.NEW))
			.collect(Collectors.toList());
	}

	List<ItemPriceChange> getDeletedItems() {
		return changes.stream()
			.filter(x -> x.getType().equals(ItemPriceChangeType.DELETE))
			.collect(Collectors.toList());
	}

	List<ItemPriceChange> getModifiedItems() {
		return changes.stream()
			.filter(x -> x.getType().equals(ItemPriceChangeType.MODIFY))
			.collect(Collectors.toList());
	}
}
