package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.app.SourceService;
import com.github.bogdanovmn.boardgameorder.web.app.UserOrder;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class UserOrderService {
	private final UserOrderItemRepository userOrderItemRepository;
	private final ItemRepository itemRepository;
	private final SourceService sourceService;

	UserOrderService(final UserOrderItemRepository userOrderItemRepository, final ItemRepository itemRepository, final SourceService sourceService) {
		this.userOrderItemRepository = userOrderItemRepository;
		this.itemRepository = itemRepository;
		this.sourceService = sourceService;
	}

	void addItem(final User user, final Integer id) {
		Item item = itemRepository.findById(id).orElseThrow(
			() -> new IllegalStateException("Item not exists: " + id)
		);

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
		Item item = itemRepository.findById(id).orElseThrow(
			() -> new IllegalStateException("Item not exists: " + id)
		);

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

	// TODO можно грузить не весь прайс, а только заказанные позиции
	UserOrderView getUserOrderView(User user, Integer sourceId) {
		return new UserOrderView(
			UserOrder.fromAllPrices(
				sourceId == null
					? sourceService.actualPrices()
					: sourceService.prices(sourceId),
				userOrderItemRepository.getAllByUser(user)
			)
		);
	}
}
