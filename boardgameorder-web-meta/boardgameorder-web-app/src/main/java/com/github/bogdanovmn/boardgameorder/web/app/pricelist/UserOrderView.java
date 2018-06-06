package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.UserOrderItem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class UserOrderView {
	private final List<ItemPrice> itemPrices;
	private final Map<Integer, Integer> orderedItems;

	private UserOrderView(final List<ItemPrice> itemPrices, final Map<Integer, Integer> orderedItems) {
		this.itemPrices = itemPrices;
		this.orderedItems = orderedItems;
	}

	static UserOrderView fromAllPrices(final List<ItemPrice> itemPrices, final List<UserOrderItem> userOrderItems) {
		Map<Integer, Integer> orderedItems = userOrderItems.stream()
			.collect(
				Collectors.toMap(x -> x.getItem().getId(), UserOrderItem::getCount)
			);

		return new UserOrderView(
			itemPrices.stream()
				.filter(x -> orderedItems.containsKey(x.getItem().getId()))
				.collect(Collectors.toList()),
			orderedItems
		);
	}

	Integer getItemsCount() {
		return orderedItems.values().stream()
			.mapToInt(x -> x)
			.sum();
	}

	Integer getTotal() {
		return itemPrices.stream()
			.mapToInt(x -> x.getRoundedPrice() * orderedItems.get(x.getItem().getId()))
			.sum();
	}

	List<OrderItemView> getItems() {
		return itemPrices.stream()
			.map(
				price -> new OrderItemView(
					price,
					orderedItems.get(
						price.getItem().getId()
					)
				)
			)
			.collect(Collectors.toList());
	}
}
