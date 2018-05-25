package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
class PriceListService {
	private final ItemRepository itemRepository;

	PriceListService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	Map<String, Object> getAllItems() {
		Map<String, Object> result = new HashMap<>();

		result.put("publishers",
			itemRepository.findAll()
//				.stream()
//				.collect(
//					Collectors.groupingBy(Item::getPublisher, Collectors.toList())
//				)
		);

		return result;
	}
}
