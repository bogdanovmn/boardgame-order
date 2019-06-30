package com.github.bogdanovmn.boardgameorder.web.app;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserOrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserOrder {
	private final Map<Integer, ItemPrice> itemPrices;
	private final Map<Integer, Integer> orderedItems;

	private UserOrder(final Map<Integer, ItemPrice> itemPrices, final Map<Integer, Integer> orderedItems) {
		this.itemPrices = itemPrices;
		this.orderedItems = orderedItems;
	}

	public static UserOrder fromAllPrices(final List<ItemPrice> itemPrices, final List<UserOrderItem> userOrderItems) {
		Map<Integer, Integer> orderedItems = userOrderItems.stream()
			.collect(
				Collectors.toMap(x -> x.getItem().getId(), UserOrderItem::getCount)
			);

		Map<Integer, ItemPrice> prices = itemPrices.stream()
			.filter(x -> orderedItems.containsKey(x.getItem().getId()))
			.collect(Collectors.toMap(
				x -> x.getItem().getId(), x -> x
			));

		for (Integer itemId : new ArrayList<>(orderedItems.keySet())) {
			if (!prices.containsKey(itemId)) {
				orderedItems.remove(itemId);
			}
		}

		return new UserOrder(prices, orderedItems);
	}

	public Integer getItemsCount() {
		return getTotal() > 0
			? orderedItems.values().stream()
				.mapToInt(x -> x)
				.sum()
			: 0;
	}

	public Integer getTotal() {
		return itemPrices.values().stream()
			.mapToInt(x -> x.getRoundedPrice() * orderedItems.get(x.getItem().getId()))
			.sum();
	}

	public Integer getTotalWithFixPrice() {
		return itemPrices.values().stream()
			.filter(x -> x.getItem().isLikeFixPriceTitle())
			.mapToInt(x -> x.getRoundedPrice() * orderedItems.get(x.getItem().getId()))
			.sum();
	}

	public Set<Integer> items() {
		return orderedItems.keySet();
	}

	public ItemPrice itemPrice(final Integer itemId) {
		return itemPrices.get(itemId);
	}

	public Integer itemCount(final Integer itemId) {
		return orderedItems.get(itemId);
	}
}
