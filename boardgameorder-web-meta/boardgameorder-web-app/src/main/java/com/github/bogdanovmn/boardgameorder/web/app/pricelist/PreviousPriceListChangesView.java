package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPriceChange;
import com.github.bogdanovmn.boardgameorder.web.orm.ItemPriceChangeType;
import com.github.bogdanovmn.boardgameorder.web.orm.Publisher;
import com.github.bogdanovmn.boardgameorder.web.orm.Source;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PreviousPriceListChangesView {
	private final List<ItemPriceChange> changes;

	PreviousPriceListChangesView(final List<ItemPriceChange> changes) {
		this.changes = changes;
	}

	Source getSource() {
		return changes.get(0).getSource();
	}

	private List<PublisherPriceChangeView> getItems(ItemPriceChangeType type) {
		return changes.stream()
			.filter(x -> x.getType().equals(type))
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

	List<PublisherPriceChangeView> getNewItems() {
		return getItems(ItemPriceChangeType.NEW);
	}

	List<PublisherPriceChangeView> getDeletedItems() {
		return getItems(ItemPriceChangeType.DELETE);
	}

	List<PublisherPriceChangeView> getModifiedItems() {
		return getItems(ItemPriceChangeType.MODIFY);
	}
}
