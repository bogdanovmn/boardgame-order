package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.SourceService;
import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PlService {
	private final UserOrderItemRepository userOrderItemRepository;
	private final ItemPriceChangeRepository itemPriceChangeRepository;
	private final SourceService sourceService;

	PlService(
		final UserOrderItemRepository userOrderItemRepository,
		final ItemPriceChangeRepository itemPriceChangeRepository,
		final SourceService sourceService
	) {
		this.userOrderItemRepository = userOrderItemRepository;
		this.itemPriceChangeRepository = itemPriceChangeRepository;
		this.sourceService = sourceService;
	}

	PlView actualPriceListView(User user) {
		List<ItemPrice> itemPrices = sourceService.actualPrices();
		List<UserOrderItem> userOrderItems = userOrderItemRepository.getAllByUser(user);

		return new PlView(itemPrices, userOrderItems);
	}

	PlChangesView priceListLastChangesView(PlChangesFilter filter) {
		ItemPriceChange itemPriceChange = itemPriceChangeRepository.findFirstByOrderBySourceIdDesc();
		Source source = itemPriceChange.getSource();
		return new PlChangesView(
			itemPriceChangeRepository.findAllBySource(source),
			filter
		);
	}
}