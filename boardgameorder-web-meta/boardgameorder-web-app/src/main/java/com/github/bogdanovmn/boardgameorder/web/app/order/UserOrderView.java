package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.app.UserOrder;

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

	List<OrderItemView> getItems() {
		return userOrder.items().stream()
			.map(
				itemId -> new OrderItemView(
					userOrder.itemPrice(itemId),
					userOrder.itemCount(itemId)
				)
			)
			.collect(Collectors.toList());
	}
}
