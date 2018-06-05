package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.UserOrderItem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class UserOrderView {
	private final List<ItemPrice> itemPrices;
	private final List<UserOrderItem> userOrderItems;

	UserOrderView(final List<ItemPrice> itemPrices, final List<UserOrderItem> userOrderItems) {
		this.itemPrices = itemPrices;
		this.userOrderItems = userOrderItems;
	}

	Integer getItemsCount() {
		return userOrderItems.stream()
			.mapToInt(UserOrderItem::getCount)
			.sum();
	}

	Integer getTotal() {
		Map<Integer, Integer> orderedItems = userOrderItems.stream()
			.collect(
				Collectors.toMap(x -> x.getItem().getId(), UserOrderItem::getCount)
			);

		return itemPrices.stream()
			.filter(x -> orderedItems.containsKey(x.getItem().getId()))
			.mapToInt(x -> x.getRoundedPrice() * orderedItems.get(x.getItem().getId()))
			.sum();
	}
}
