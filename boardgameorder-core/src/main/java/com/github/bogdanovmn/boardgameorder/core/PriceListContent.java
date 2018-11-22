package com.github.bogdanovmn.boardgameorder.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PriceListContent {
	private final static Pattern BOARD_GAME = Pattern.compile(
		"^.*(наст.*игр|игр.*наст|протекторы|дополнение|доп.*игр).*$",
			Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
	);
	private final static Pattern ZVEZDA_TANK_MODELS = Pattern.compile("^.*танк.*[тt]-\\d.*$", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
	private final static Pattern ZVEZDA_MODELS =
		Pattern.compile(
			"^Зв\\.\\d+[a-zа-я]*\\s*(" +
				"(БТР|Бомбардировщик|Мотоцикл|Грузовик|Галеон|Пассажирский|Подводная|Танк|Пехота|Самол.т|Эсминец|Самоходка|Авианосец|Авиалайнер|Корабль|Линкор|Вертол.т)|" +
				"((Фр|Росс|Амер|Нем|Немец|Сов|Брит)\\.)|" +
				"((Польск|Английск|Русск|Испанск|Французс?к|Американск|Советск|Немецк|Британск|Росс?ийск)(ие|ая|ий|ое))" +
			").*$",
			Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
	);

	private final PriceListExcelFile file;

	public PriceListContent(PriceListExcelFile file) {
		this.file = file;
	}

	public List<ExcelPriceItem> boardGames() {
		return file.priceItems().stream()
			.filter(x ->
				(BOARD_GAME.matcher(x.getTitle()).find() ||
					BOARD_GAME.matcher(x.getGroupOriginal()).find())
						&& !x.getGroupOriginal().contains("УЦЕНКА")
						&& !x.getTitle().contains("склейка")
						&& !ZVEZDA_MODELS.matcher(x.getTitle()).find()
						&& !ZVEZDA_TANK_MODELS.matcher(x.getTitle()).find()
			)
			.collect(Collectors.toList());
	}

	public void printTotal() {
		Map<String, Integer> groupStat = new HashMap<>();
		boardGames().forEach(
			row -> groupStat.compute(
				row.getGroupOriginal(),
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
