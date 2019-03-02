package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.*;
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
class PriceListChangesService {
	private static final Logger LOG = LoggerFactory.getLogger(PriceListChangesService.class);

	private final SourceRepository sourceRepository;
	private final ItemPriceRepository itemPriceRepository;
	private final ItemPriceChangeRepository itemPriceChangeRepository;

	PriceListChangesService(
		SourceRepository sourceRepository,
		ItemPriceRepository itemPriceRepository,
		ItemPriceChangeRepository itemPriceChangeRepository
	) {
		this.sourceRepository = sourceRepository;
		this.itemPriceRepository = itemPriceRepository;
		this.itemPriceChangeRepository = itemPriceChangeRepository;
	}


	@Transactional(rollbackFor = Exception.class)
	public void updateAllChanges() {
		List<Source> sources = sourceRepository.findAllByOrderByFileModifyDateDesc();

		if (sources.size() > 1) {
			itemPriceChangeRepository.deleteAll();
			itemPriceChangeRepository.flush();
			for (int i = 1; i < sources.size(); i++) {
				LOG.info("Update changes between {} and {}", sources.get(i - 1), sources.get(i));
				updateChanges(sources.get(i), sources.get(i - 1));
			}
		}
		else {
			LOG.info("No changes found, price lists count: {}", sources.size());
		}
	}

	private void updateChanges(final Source previousSource, final Source source) {
		Map<Integer, ItemPrice> sourcePrices = itemPriceRepository.findBySourceId(source.getId()).stream()
			.collect(Collectors.toMap(
				x -> x.getItem().getId(), x -> x
			));
		Map<Integer, ItemPrice> prevSourcePrices = itemPriceRepository.findBySourceId(previousSource.getId()).stream()
			.collect(Collectors.toMap(
				x -> x.getItem().getId(), x -> x
			));

		ItemPriceChange priceChangeTemplate = new ItemPriceChange()
			.setSource(source)
			.setPreviousSource(previousSource);

		Set<ItemPriceChange> deletedPrices = Sets.difference(
			prevSourcePrices.keySet(),
			sourcePrices.keySet()
		).stream()
			.map(x -> priceChangeTemplate.copy()
				.setType(ItemPriceChangeType.DELETE)
				.setItem(prevSourcePrices.get(x).getItem())
			)
			.collect(Collectors.toSet());

		Set<ItemPriceChange> newPrices = Sets.difference(
			sourcePrices.keySet(),
			prevSourcePrices.keySet()
		).stream()
			.map(x -> {
				ItemPrice itemPrice = sourcePrices.get(x);
				return priceChangeTemplate.copy()
					.setType(ItemPriceChangeType.NEW)
					.setItem(itemPrice.getItem())
					.setCountCurrent(itemPrice.getCount())
					.setPriceCurrent(itemPrice.getPrice());
				}
			)
			.collect(Collectors.toSet());

		Sets.SetView<Integer> samePrices = Sets.intersection(sourcePrices.keySet(), prevSourcePrices.keySet());
		Set<ItemPriceChange> modifiedPrices = new HashSet<>();
		samePrices.forEach(
			itemId -> {
				ItemPrice prev = prevSourcePrices.get(itemId);
				ItemPrice curr = sourcePrices.get(itemId);

				boolean hasChanges = false;
				ItemPriceChange change = priceChangeTemplate.copy();

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
							.setType(ItemPriceChangeType.MODIFY)
							.setItem(curr.getItem())
							.setCountCurrent(curr.getCount())
							.setPriceCurrent(curr.getPrice())
					);
				}
			});

		LOG.info(
			"New items: {}, deleted items: {}, modified items: {}",
			newPrices.size(), deletedPrices.size(), modifiedPrices.size()
		);

		if (!newPrices.isEmpty()) {
			itemPriceChangeRepository.saveAll(newPrices);
		}
		if (!deletedPrices.isEmpty()) {
			itemPriceChangeRepository.saveAll(deletedPrices);
		}
		if (!modifiedPrices.isEmpty()) {
			itemPriceChangeRepository.saveAll(modifiedPrices);
		}
		LOG.info("Changes update is done");
	}
}
