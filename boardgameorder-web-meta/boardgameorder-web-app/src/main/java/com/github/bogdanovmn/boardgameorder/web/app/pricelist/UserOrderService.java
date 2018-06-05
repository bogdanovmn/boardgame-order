package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
class UserOrderService {
	private final UserOrderItemRepository userOrderItemRepository;
	private final ItemRepository itemRepository;

	UserOrderService(final UserOrderItemRepository userOrderItemRepository, final ItemRepository itemRepository) {
		this.userOrderItemRepository = userOrderItemRepository;
		this.itemRepository = itemRepository;
	}

	void addItem(final User user, final Integer id) {
		Item item = itemRepository.findOne(id);
		if (item == null) {
			throw new IllegalStateException("Item not exists: " + id);
		}

		UserOrderItem orderItem = userOrderItemRepository.findFirstByUserAndItem(user, item);
		if (orderItem == null) {
			userOrderItemRepository.save(
				new UserOrderItem()
					.setUser(user)
					.setItem(item)
					.setCount(1)
			);
		}
		else {
			userOrderItemRepository.save(
				orderItem.incCount(1)
			);
		}
	}

	void deleteItem(final User user, final Integer id) {
		Item item = itemRepository.findOne(id);
		if (item == null) {
			throw new IllegalStateException("Item not exists: " + id);
		}

		UserOrderItem orderItem = userOrderItemRepository.findFirstByUserAndItem(user, item);
		if (orderItem == null) {
			throw new IllegalStateException("Item order not exists: " + id);
		}
		else {
			orderItem.incCount(-1);
			if (orderItem.getCount() == 0) {
				userOrderItemRepository.delete(orderItem);
			}
			else {
				userOrderItemRepository.save(orderItem.setUpdated(new Date()));
			}
		}
	}

	List<UserOrderItem> getAllItems(final User user) {
		return userOrderItemRepository.getAllByUser(user);
	}
}
