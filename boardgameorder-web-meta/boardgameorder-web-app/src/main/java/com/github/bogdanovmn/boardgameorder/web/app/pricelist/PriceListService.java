package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PriceListService {
	private static final Logger LOG = LoggerFactory.getLogger(PriceListService.class);

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
		Source source = sourceRepository.findTopByOrderByFileModifyDateDesc();
		return itemPriceRepository.findBySource(source);
	}

	PriceListChangesView priceListChangesView() {
		return PriceListChangesView.fromFullList(
			itemPriceChangeRepository.findAll()
		);
	}
}
