package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.Publisher;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class PublisherPriceView {
	private final Publisher publisher;
	private final List<ItemPrice> prices;

	PublisherPriceView(final Publisher publisher, final List<ItemPrice> prices) {
		this.publisher = publisher;
		this.prices = prices;
	}

	Publisher getPublisher() {
		return publisher;
	}

	List<ItemPrice> getPrices() {
		return prices.stream()
			.sorted(
				Comparator.comparing(
					(ItemPrice x) ->
						x.getItem().isLikeBoardGameTitle()
				)
					.reversed()
					.thenComparing(
						(ItemPrice x) ->
							x.getItem().getEffectiveTitle()
					)
			)
			.collect(Collectors.toList());
	}
}
