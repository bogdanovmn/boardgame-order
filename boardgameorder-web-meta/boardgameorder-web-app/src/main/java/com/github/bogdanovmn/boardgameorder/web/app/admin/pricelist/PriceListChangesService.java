package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.*;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PriceListChangesService {
	private static final Logger LOG = LoggerFactory.getLogger(PriceListChangesService.class);

	private final SourceRepository sourceRepository;
	private final ItemPriceRepository itemPriceRepository;
	private final ItemPriceChangeRepository itemPriceChangeRepository;

	public PriceListChangesService(
		SourceRepository sourceRepository,
		ItemPriceRepository itemPriceRepository,
		ItemPriceChangeRepository itemPriceChangeRepository
	) {
		this.sourceRepository = sourceRepository;
		this.itemPriceRepository = itemPriceRepository;
		this.itemPriceChangeRepository = itemPriceChangeRepository;
	}


	@Transactional(rollbackFor = Exception.class)
	void updateAllChanges() {
		List<Source> sources = sourceRepository.findAllByOrderByFileModifyDateDesc();

		if (sources.size() > 1) {
			itemPriceChangeRepository.deleteAll();
			for (int i = 1; i < sources.size(); i++) {
				LOG.info("Update changes between {} and {}", sources.get(i - 1), sources.get(i));
				updateChanges(sources.get(i - 1), sources.get(i));
			}
		}
		else {
			LOG.info("No changes found, price lists count: {}", sources.size());
		}
	}

	private void updateChanges(final Source previousSource, final Source source) {
		Map<Integer, ItemPrice> sourcePrices = itemPriceRepository.findBySource(source).stream()
			.collect(Collectors.toMap(
				ItemPrice::getId, x -> x
			));
		Map<Integer, ItemPrice> prevSourcePrices = itemPriceRepository.findBySource(previousSource).stream()
			.collect(Collectors.toMap(
				ItemPrice::getId, x -> x
			));

		Set<ItemPriceChange> deletedPrices = Sets.difference(
			prevSourcePrices.keySet(),
			sourcePrices.keySet()
		).stream()
			.map(x -> new ItemPriceChange()
				.setType(ItemPriceChangeType.DELETE)
				.setPreviousSource(previousSource)
				.setSource(source)
				.setItem(prevSourcePrices.get(x).getItem())
			)
			.collect(Collectors.toSet());

		Set<ItemPriceChange> newPrices = Sets.difference(
			sourcePrices.keySet(),
			prevSourcePrices.keySet()
		).stream()
			.map(x -> new ItemPriceChange()
				.setType(ItemPriceChangeType.NEW)
				.setPreviousSource(previousSource)
				.setSource(source)
				.setItem(sourcePrices.get(x).getItem())
			)
			.collect(Collectors.toSet());

		Sets.SetView<Integer> samePrices = Sets.intersection(sourcePrices.keySet(), prevSourcePrices.keySet());
		Set<ItemPriceChange> modifiedPrices = new HashSet<>();
		samePrices.forEach(
			itemId -> {
				ItemPrice prev = prevSourcePrices.get(itemId);
				ItemPrice curr = sourcePrices.get(itemId);

				boolean hasChanges = false;
				ItemPriceChange change = new ItemPriceChange();

				if (!prev.getPrice().equals(curr.getPrice())) {
					change.setPriceChange(
						curr.getPrice() - prev.getPrice()
					);
					hasChanges = true;
				}

				if (!prev.getCount().equals(curr.getCount())) {
					change.setCountChange(
						curr.getCount() - prev.getCount()
					);
					hasChanges = true;
				}

				if (hasChanges) {
					modifiedPrices.add(
						change
							.setSource(source)
							.setPreviousSource(previousSource)
							.setItem(curr.getItem())
							.setType(ItemPriceChangeType.MODIFY)
					);
				}
			});

		LOG.info(
			"New items: {}, deleted items: {}, modified items: {}",
			newPrices.size(), deletedPrices.size(), modifiedPrices.size()
		);

		if (!newPrices.isEmpty()) {
			itemPriceChangeRepository.save(newPrices);
		}
		if (!deletedPrices.isEmpty()) {
			itemPriceChangeRepository.save(deletedPrices);
		}
		if (!modifiedPrices.isEmpty()) {
			itemPriceChangeRepository.save(modifiedPrices);
		}
		LOG.info("Changes update is done");
	}
}
