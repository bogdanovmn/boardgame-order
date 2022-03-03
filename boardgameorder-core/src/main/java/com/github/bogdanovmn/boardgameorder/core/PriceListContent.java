package com.github.bogdanovmn.boardgameorder.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PriceListContent {
    private static final Pattern BOARD_GAME = Pattern.compile(
        "^.*(наст.*игр|игр.*наст|протекторы|дополнение|доп.*игр|логич.*игра).*$",
        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
    );
    private static final Pattern ZVEZDA_TANK_MODELS = Pattern.compile("^.*танк.*[тt]-\\d.*$", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    private static final Pattern ZVEZDA_MODELS = Pattern.compile(
        "^Зв\\.\\d+[a-zа-я]*\\s*("
            + "(БТР|Бомбардировщик|Мотоцикл|Грузовик|Галеон|Пассажирский|Подводная|Танк|Пехота|Самол.т|Эсминец|Самоходка|Авианосец|Авиалайнер|Корабль|Линкор|Вертол.т)|"
            + "((Фр|Росс|Амер|Нем|Немец|Сов|Брит)\\.)|"
            + "((Польск|Английск|Русск|Испанск|Французс?к|Американск|Советск|Немецк|Британск|Росс?ийск)(ие|ая|ий|ое))"
        + ").*$",
        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
    );

    private final PriceListExcelFile file;

    public PriceListContent(PriceListExcelFile file) {
        this.file = file;
    }

    public List<ExcelPriceItem> boardGames() {
        return file.priceItems().stream()
            .filter(item ->
                (
                    BOARD_GAME.matcher(item.getTitle()).find()
                    || BOARD_GAME.matcher(item.getGroupOriginal()).find()
                )
                    && !item.getGroupOriginal().contains("УЦЕНКА")
                    && !item.getTitle().contains("склейка")
                    && !ZVEZDA_MODELS.matcher(item.getTitle()).find()
                    && !ZVEZDA_TANK_MODELS.matcher(item.getTitle()).find()
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
                System.out.printf("[%3d] %s%n", groupStat.get(x), x)
            );
    }


}
