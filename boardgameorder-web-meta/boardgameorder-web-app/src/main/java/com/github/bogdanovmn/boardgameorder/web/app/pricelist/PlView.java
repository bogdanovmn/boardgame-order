package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.UserOrder;
import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.UserOrderItem;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class PlView {
	private final List<ItemPrice> itemPrices;
	private final List<UserOrderItem> userOrderItems;

	PlView(final List<ItemPrice> itemPrices, final List<UserOrderItem> userOrderItems) {
		this.itemPrices = itemPrices;
		this.userOrderItems = userOrderItems;
	}

	List<PlPublisherView> getPublisherPrices() {
		return itemPrices.stream()
			.collect(
				Collectors.groupingBy(
					ip -> ip.getItem().getPublisher(),
					Collectors.toList()
				)
			).entrySet().stream()
			.map(
				x -> new PlPublisherView(x.getKey(), x.getValue(), userOrderItems)
			)
			.sorted(
				Comparator.comparing(
					(PlPublisherView x) -> x.getPrices().size()
				).reversed()
			)
			.collect(Collectors.toList());
	}

	UserOrder getUserOrder() {
		return UserOrder.fromAllPrices(itemPrices, userOrderItems);
	}
}
