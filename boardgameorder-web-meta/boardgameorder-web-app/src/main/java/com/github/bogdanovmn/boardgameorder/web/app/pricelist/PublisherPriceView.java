package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.Publisher;
import com.github.bogdanovmn.boardgameorder.web.orm.UserOrderItem;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class PublisherPriceView {
	private final Publisher publisher;
	private final List<ItemPrice> prices;
	private final List<UserOrderItem> userOrderItems;

	PublisherPriceView(final Publisher publisher, final List<ItemPrice> prices, final List<UserOrderItem> userOrderItems) {
		this.publisher = publisher;
		this.prices = prices;
		this.userOrderItems = userOrderItems;
	}

	Publisher getPublisher() {
		return publisher;
	}

	List<ItemPriceView> getPrices() {
		Set<Integer> ordered = userOrderItems.stream().map(x -> x.getItem().getId()).collect(Collectors.toSet());
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
			.map(x -> new ItemPriceView(x, ordered.contains(x.getItem().getId())))
			.collect(Collectors.toList());
	}
}
