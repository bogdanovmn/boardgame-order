package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.ItemPriceRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.Source;
import com.github.bogdanovmn.boardgameorder.web.orm.SourceRepository;
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

	PriceListService(final SourceRepository sourceRepository, final ItemPriceRepository itemPriceRepository) {
		this.sourceRepository = sourceRepository;
		this.itemPriceRepository = itemPriceRepository;
	}

	Map<String, Object> actualPriceList() {
		Source source = sourceRepository.findTopByOrderByFileModifyDateDesc();
		List<ItemPrice> itemPrices = itemPriceRepository.findBySource(source);

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

		return result;
	}
}
