package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.UserOrderItem;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class PriceListView {
	private final List<ItemPrice> itemPrices;
	private final List<UserOrderItem> userOrderItems;

	PriceListView(final List<ItemPrice> itemPrices, final List<UserOrderItem> userOrderItems) {

		this.itemPrices = itemPrices;
		this.userOrderItems = userOrderItems;
	}

	List<PublisherPriceView> getPublisherPrices() {
		return itemPrices.stream()
			.collect(
				Collectors.groupingBy(
					ip -> ip.getItem().getPublisher(),
					Collectors.toList()
				)
			).entrySet().stream()
			.map(
				x -> new PublisherPriceView(x.getKey(), x.getValue())
			)
			.sorted(
				Comparator.comparing(
					(PublisherPriceView x) -> x.getPrices().size()
				).reversed()
			)
			.collect(Collectors.toList());
	}

	UserOrderView getUserOrder() {
		return UserOrderView.fromAllPrices(itemPrices, userOrderItems);
	}
}
