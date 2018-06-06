package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class UserOrderService {
	private final UserOrderItemRepository userOrderItemRepository;
	private final ItemRepository itemRepository;
	private final PriceListService priceListService;

	UserOrderService(final UserOrderItemRepository userOrderItemRepository, final ItemRepository itemRepository, final PriceListService priceListService) {
		this.userOrderItemRepository = userOrderItemRepository;
		this.itemRepository = itemRepository;
		this.priceListService = priceListService;
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

	// TODO можно грузить не весь прайс, а только заказанные позиции
	UserOrderView getUserOrderView(final User user) {
		return UserOrderView.fromAllPrices(
			priceListService.getActualPriceList(),
			userOrderItemRepository.getAllByUser(user)
		);
	}
}
