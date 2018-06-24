package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPriceChange;
import com.github.bogdanovmn.boardgameorder.web.orm.Publisher;
import com.github.bogdanovmn.boardgameorder.web.orm.Source;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PlChangesView {
	private final List<ItemPriceChange> allChanges;
	private final PlChangesFilter filter;

	PlChangesView(final List<ItemPriceChange> allChanges, final PlChangesFilter filter) {
		this.allChanges = allChanges;
		this.filter = filter;
	}

	Source getSource() {
		return allChanges.get(0).getSource();
	}

	List<PublisherPriceChangeView> getItems() {
		return allChanges.stream()
			.filter(filter::apply)
			.collect(
				Collectors.groupingBy(
					x -> x.getItem().getPublisher(),
					Collectors.toList()
				)
			).entrySet().stream()
				.sorted(
					Comparator.comparing(
						(Map.Entry<Publisher, List<ItemPriceChange>> x) -> x.getValue().size()
					).reversed()
				)
				.map(x ->
					new PublisherPriceChangeView(
						x.getKey(),
						x.getValue()
					)
				)
				.collect(Collectors.toList());
	}
}
