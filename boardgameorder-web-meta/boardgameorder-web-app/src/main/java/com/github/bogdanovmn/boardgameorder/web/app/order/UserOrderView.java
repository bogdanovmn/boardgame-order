package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.app.UserOrder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class UserOrderView {
	private final UserOrder userOrder;

	UserOrderView(UserOrder userOrder) {
		this.userOrder = userOrder;
	}

	Integer getItemsCount() {
		return userOrder.getItemsCount();
	}

	Integer getTotal() {
		return userOrder.getTotal();
	}

	Integer getTotalWithFixPrice() {
		return userOrder.getTotalWithFixPrice();
	}

	List<OrderItemView> getItems() {
		return userOrder.items().stream()
			.sorted(
				Comparator.comparing(itemId ->
					userOrder.itemPrice(itemId).getItem().getEffectiveTitle()
				)
			)
			.map(
				itemId -> new OrderItemView(
					userOrder.itemPrice(itemId),
					userOrder.itemCount(itemId)
				)
			)
			.collect(Collectors.toList());
	}
}
