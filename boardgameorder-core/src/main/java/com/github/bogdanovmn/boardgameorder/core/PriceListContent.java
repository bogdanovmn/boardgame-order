package com.github.bogdanovmn.boardgameorder.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PriceListContent {
	private final static Pattern BOARD_GAME = Pattern.compile("^.*(наст.*игр|игр.*наст).*$", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

	private final PriceListExcelFile file;

	private final Map<String, PriceItem> items = new HashMap<>();

	public PriceListContent(PriceListExcelFile file) {
		this.file = file;
	}

	public List<PriceItem> boardGames() {
		return file.priceItems().stream()
			.filter(x ->
				BOARD_GAME.matcher(x.getTitle()).find() ||
					BOARD_GAME.matcher(x.getGroup()).find())
			.collect(Collectors.toList());
	}

	public void printTotal() {
		Map<String, Integer> groupStat = new HashMap<>();
		boardGames().forEach(
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
