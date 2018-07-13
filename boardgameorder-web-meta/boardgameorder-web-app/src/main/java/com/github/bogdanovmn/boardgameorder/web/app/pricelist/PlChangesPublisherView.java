package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPriceChange;
import com.github.bogdanovmn.boardgameorder.web.orm.Publisher;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class PlChangesPublisherView {
	private final Publisher publisher;
	private final List<ItemPriceChange> itemPriceChanges;

	PlChangesPublisherView(final Publisher publisher, final List<ItemPriceChange> changes) {
		this.publisher = publisher;
		this.itemPriceChanges = changes;
	}

	Publisher getPublisher() {
		return publisher;
	}

	List<ItemPriceChange> getChanges() {
		return itemPriceChanges.stream()
			.sorted(
				Comparator.comparing(
					(ItemPriceChange x) ->
						x.getItem().isLikeBoardGameTitle()
				)
					.reversed()
					.thenComparing(
						(ItemPriceChange x) ->
							x.getItem().getEffectiveTitle()
					)
			)
			.collect(Collectors.toList());
	}
}
