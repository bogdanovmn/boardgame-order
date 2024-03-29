package com.github.bogdanovmn.boardgameorder.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class PriceListColumnMap {
    private static final Pattern COUNT_PATTERN = Pattern.compile(
        "(кол-во|наличие|остаток)",
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );
    private static final Pattern PRICE_PATTERN = Pattern.compile(
        "^\\s*(цена|база|базовая цена|базовый опт)\\s*$",
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );
    private static final Pattern BARCODE_PATTERN = Pattern.compile("штрих", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern PHOTO_PATTERN = Pattern.compile("фото", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern NAME_PATTERN = Pattern.compile("номенклатура", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private final Map<String, Integer> columnMap = new HashMap<>();

    void put(final String title, final int index) {
        columnMap.put(title, index);
    }

    boolean isLooksLikeNameColumn(String columnTitle) {
        return NAME_PATTERN.matcher(columnTitle).find();
    }

    Integer getNameIndex() {
        return getColumnIndex("Name", NAME_PATTERN);
    }

    Integer getPriceIndex() {
        return getColumnIndex("Price", PRICE_PATTERN);
    }

    Integer getCountIndex() {
        return getColumnIndex("Count", COUNT_PATTERN);
    }

    Integer getBarcodeIndex() {
        return getColumnIndex("Barcode", BARCODE_PATTERN);
    }

    Integer getPhotoIndex() {
        return getColumnIndex("Photo", PHOTO_PATTERN);
    }

    private Integer getColumnIndex(final String alias, final Pattern pattern) {
        List<String> titles = columnMap.keySet().stream()
            .filter(x -> pattern.matcher(x).find())
            .collect(Collectors.toList());

        if (titles.size() != 1) {
            throw new RuntimeException(
                String.format(
                    "%s column not found: %s",
                    alias, columnMap.keySet()
                )
            );
        }

        return columnMap.get(titles.get(0));
    }

}
