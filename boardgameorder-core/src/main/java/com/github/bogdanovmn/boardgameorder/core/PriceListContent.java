package com.github.bogdanovmn.boardgameorder.core;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PriceListContent {
	private final PriceListExcelFile file;

	private final Map<String, ItemRow> items = new HashMap<>();

	public PriceListContent(PriceListExcelFile file) {
		this.file = file;
	}

	public void printTotal() throws IOException {
		Map<String, Integer> groupStat = new HashMap<>();
		file.itemRows().forEach(
			row -> groupStat.compute(
				row.getGroup(),
				(k, v) -> v == null ? 1 : v + 1
			)
		);

		groupStat.keySet().stream()
			.sorted(
				Comparator.comparingInt(groupStat::get)
			)
			.forEach(x ->
				System.out.println(
					String.format(
						"[%3d] %s",
							groupStat.get(x), x
					)
				)
			);
	}


}
