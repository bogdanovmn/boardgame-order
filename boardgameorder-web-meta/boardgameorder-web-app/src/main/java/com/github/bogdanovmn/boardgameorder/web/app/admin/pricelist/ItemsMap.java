package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.core.ExcelPriceItem;
import com.github.bogdanovmn.boardgameorder.web.orm.Item;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ItemsMap {
	private final Map<String, List<Item>> mapByBarcode;
	private final Map<String, Item> mapByTitle;

	ItemsMap(final List<Item> items) {
		this.mapByBarcode = items.stream()
			.filter(x -> x.getBarcode() != null)
			.collect(Collectors.groupingBy(
				Item::getBarcode, Collectors.toList()
			));
		this.mapByTitle = items.stream()
			.collect(Collectors.toMap(
				Item::getTitle, x -> x
			));
	}

	Item get(final ExcelPriceItem excelPriceItem) {
		Item result;
		if (excelPriceItem.getBarcode() == null) {
			result = getByTitle(excelPriceItem);
		}
		else {
			result = getByBarcode(excelPriceItem);
			Item itemByTitle = getByTitle(excelPriceItem);
			if (result != null) {
				if (itemByTitle != null) {
					if (!itemByTitle.getId().equals(result.getId())) {
						throw new RuntimeException(
							String.format(
								"Barcode violation %s for items: %s, %s",
								excelPriceItem, result, itemByTitle

							)
						);
					}
				}
			}
			else if (itemByTitle != null && itemByTitle.getBarcode() == null) {
				result = itemByTitle;
			}
		}
		return result;
	}

	private Item getByTitle(ExcelPriceItem excelPriceItem) {
		return mapByTitle.get(excelPriceItem.getTitle());
	}

	private Item getByBarcode(ExcelPriceItem excelPriceItem) {
		List<Item> items = mapByBarcode.get(excelPriceItem.getBarcode());
		if (items != null) {
			items = items.stream()
				.filter(x -> x.getPublisher().getName().equals(excelPriceItem.getGroup()))
				.collect(Collectors.toList());
		}

		return items != null && items.size() == 1
			? items.get(0)
			: null;
	}
}
