package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PriceListService {
	private final SourceRepository sourceRepository;
	private final ItemPriceRepository itemPriceRepository;
	private final UserOrderItemRepository userOrderItemRepository;
	private final ItemPriceChangeRepository itemPriceChangeRepository;

	PriceListService(
		final SourceRepository sourceRepository,
		final ItemPriceRepository itemPriceRepository,
		final UserOrderItemRepository userOrderItemRepository,
		final ItemPriceChangeRepository itemPriceChangeRepository)
	{
		this.sourceRepository = sourceRepository;
		this.itemPriceRepository = itemPriceRepository;
		this.userOrderItemRepository = userOrderItemRepository;
		this.itemPriceChangeRepository = itemPriceChangeRepository;
	}

	PriceListView actualPriceListView(User user) {
		List<ItemPrice> itemPrices = getActualPriceList();
		List<UserOrderItem> userOrderItems = userOrderItemRepository.getAllByUser(user);

		return new PriceListView(itemPrices, userOrderItems);
	}

	List<ItemPrice> getActualPriceList() {
		Source source = getActualSource();
		return itemPriceRepository.findBySource(source);
	}

	PlChangesView priceListLastChangesView(PlChangesFilter filter) {
		ItemPriceChange itemPriceChange = itemPriceChangeRepository.findFirstByOrderBySourceIdDesc();
		Source source = itemPriceChange.getSource();
		return new PlChangesView(
			itemPriceChangeRepository.findAllBySource(source),
			filter
		);
	}

	private Source getActualSource() {
		return sourceRepository.findTopByOrderByFileModifyDateDesc();
	}
}
