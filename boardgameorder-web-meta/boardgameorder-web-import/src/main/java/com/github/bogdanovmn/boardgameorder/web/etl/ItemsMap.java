package com.github.bogdanovmn.boardgameorder.web.etl;

import com.github.bogdanovmn.boardgameorder.core.ExcelPriceItem;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ItemsMap {
	private static final Logger LOG = LoggerFactory.getLogger(ItemsMap.class);

	private final Map<String, List<Item>> mapByTitle;

	ItemsMap(final List<Item> items) {
		this.mapByTitle = items.stream()
			.collect(Collectors.groupingBy(
				item -> normalizeTitle(item.getTitle()),
				Collectors.toList()
			));
	}

	Item get(final ExcelPriceItem excelPriceItem) {
		return getByTitle(excelPriceItem);
	}

	private String normalizeTitle(String title) {
		return title.toLowerCase()
			.replaceAll("/\\d+", "")
			.replaceAll("м?ррц\\s*-?\\s*\\d+\\s*(руб|р|рублей)?", "")
			.replaceAll("(промо|фикс|фиксированная)\\W+цена", "")
			.replaceAll("\\Wарт\\.\\s*\\S+", "")
			.replaceAll("№\\S+", "")
			.replaceFirst("^\\s*новинка", "")
			.replaceAll("crowd games", "")
			.replaceFirst("\\bнаст(\\.\\s*|ольная)\\s*игра", "")
			.replaceFirst("\\s\\d{3,}\\s*$", "")
			.replaceFirst("\\s[a-z]+\\d{2,}\\w*", "")
			.replaceAll("\\p{P}|\\s", "");
	}

	private Item getByTitle(ExcelPriceItem excelPriceItem) {
		Item result = null;
		String normalizedTitle = normalizeTitle(
			excelPriceItem.getTitle()
		);

		LOG.debug("Normalized title: {}", normalizedTitle);

		List<Item> items = mapByTitle.get(normalizedTitle);
		if (items != null) {
			if (items.size() == 1) {
				result = items.get(0);
			}
			else {
				result = items.stream()
					.filter(x -> x.getPublisher().getName().equals(excelPriceItem.getGroup()))
					.findFirst()
					.orElse(null);
			}
		}

		return result;
	}
}
