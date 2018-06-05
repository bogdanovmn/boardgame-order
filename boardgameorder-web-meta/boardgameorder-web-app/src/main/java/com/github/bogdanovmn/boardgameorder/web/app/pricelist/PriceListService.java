package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	Map<String, Object> actualPriceList(User user) {
		Source source = sourceRepository.findTopByOrderByFileModifyDateDesc();
		List<ItemPrice> itemPrices = itemPriceRepository.findBySource(source);
		List<UserOrderItem> userOrderItems = userOrderItemRepository.getAllByUser(user);

		Map<String, Object> result = new HashMap<>();

		result.put("publishers",
			itemPrices.stream()
				.collect(
					Collectors.groupingBy(
						ip -> ip.getItem().getPublisher(),
						Collectors.toList()
					)
				).entrySet().stream()
					.map(
						x -> new PublisherPriceView(x.getKey(), x.getValue())
					)
					.sorted(
						Comparator.comparing(
							(PublisherPriceView x) -> x.getPrices().size()
						).reversed()
					)
					.collect(Collectors.toList())
		);

		result.put("order", new UserOrderView(itemPrices, userOrderItems));

		return result;
	}
}
