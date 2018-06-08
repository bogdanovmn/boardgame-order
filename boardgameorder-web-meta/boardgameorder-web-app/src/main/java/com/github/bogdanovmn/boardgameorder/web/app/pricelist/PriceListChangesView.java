package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.ItemPriceChange;
import com.github.bogdanovmn.boardgameorder.web.orm.Source;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PriceListChangesView {
	private final Map<Source, List<ItemPriceChange>> changesBySource;

	private PriceListChangesView(final Map<Source, List<ItemPriceChange>> changesBySource) {
		this.changesBySource = changesBySource;
	}

	static PriceListChangesView fromFullList(final List<ItemPriceChange> changes) {
		return new PriceListChangesView(
			changes.stream()
				.collect(
					Collectors.groupingBy(
						ItemPriceChange::getSource,
						Collectors.toList()
					)
				)
		);
	}

	List<PreviousPriceListChangesView> getAllChanges() {
		return changesBySource.keySet().stream()
			.sorted(Comparator.comparing(
				Source::getFileModifyDate)
			)
			.map(
				x -> new PreviousPriceListChangesView(
					changesBySource.get(x)
			))
			.collect(Collectors.toList());
	}
}
