package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PriceListService {
	private final SourceRepository sourceRepository;
	private final ItemPriceRepository itemPriceRepository;
	private final UserOrderItemRepository userOrderItemRepository;

	PriceListService(final SourceRepository sourceRepository, final ItemPriceRepository itemPriceRepository, final UserOrderItemRepository userOrderItemRepository) {
		this.sourceRepository = sourceRepository;
		this.itemPriceRepository = itemPriceRepository;
		this.userOrderItemRepository = userOrderItemRepository;
	}

	PriceListView actualPriceListView(User user) {
		List<ItemPrice> itemPrices = getActualPriceList();
		List<UserOrderItem> userOrderItems = userOrderItemRepository.getAllByUser(user);

		return new PriceListView(itemPrices, userOrderItems);
	}

	List<ItemPrice> getActualPriceList() {
		Source source = sourceRepository.findTopByOrderByFileModifyDateDesc();
		return itemPriceRepository.findBySource(source);
	}
}
